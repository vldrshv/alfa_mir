package com.filechooser;

import android.Manifest;
import android.content.Context;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import android.util.TypedValue;
import android.widget.Toast;
import com.filechooser.internals.FileUtil;
import com.filechooser.permissions.PermissionsUtil;
import com.filechooser.tool.FileDialogFragment;
import com.filechooser.tool.RootFile;

import java.io.File;
import java.io.FileFilter;
import java.util.*;

/**
 * Created by coco on 6/7/15.
 */
public class ChooserDialog {

    @FunctionalInterface
    public interface CanNavigateUp {
        boolean canUpTo(File dir);
    }

    @FunctionalInterface
    public interface CanNavigateTo {
        boolean canNavigate(File dir);
    }

    @FunctionalInterface
    public interface Result {
        void onChoosePath(Map<String, File> result);
    }

    private List<File> _entries = new ArrayList<>();
    private File _currentDir;
    private Context _context;
    private Result _result = null;
    private Map<String, File> selected = new HashMap<>();
    private List<Integer> selectedIds = new ArrayList<>();
    private boolean _dirOnly;
    private FileFilter _fileFilter;
    private boolean _enableOptions;
    private boolean _displayPath = true;
    private PermissionsUtil.OnPermissionListener _permissionListener;
    private CanNavigateUp _folderNavUpCB;
    private CanNavigateTo _folderNavToCB;
    private final static CanNavigateUp _defaultNavUpCB = dir -> dir != null && dir.canRead();
    private final static CanNavigateTo _defaultNavToCB = dir -> true;
    private final static String sSdcardStorage = ".. SDCard Storage";
    private final static String sPrimaryStorage = ".. Primary Storage";
    private static final int CHOOSE_MODE_NORMAL = 0;
    private static final int CHOOSE_MODE_DELETE = 1;
    private int _chooseMode = CHOOSE_MODE_NORMAL;
    private FragmentActivity activity;
    private FileDialogFragment fsDialog;

    public ChooserDialog() {
    }

    public ChooserDialog(FragmentActivity activity) {
        this._context = activity;
        this.activity = activity;
        init();
    }

    private void init() {
        init(null);
    }

    private void init(@Nullable @StyleRes Integer fileChooserTheme) {

        if (fileChooserTheme == null) {
            TypedValue typedValue = new TypedValue();
            if (!this._context.getTheme().resolveAttribute(
                    R.attr.fileChooserStyle, typedValue, true)) {
                this._context = new ContextThemeWrapper(this._context, R.style.FileChooserStyle);
            } else {
                this._context = new ContextThemeWrapper(this._context, typedValue.resourceId);
            }
        } else {
            //noinspection UnnecessaryUnboxing
            this._context = new ContextThemeWrapper(this._context, fileChooserTheme.intValue());
        }
    }

    public ChooserDialog withChosenListener(Result r) {
        this._result = r;
        return this;
    }

    public ChooserDialog displayPath(boolean displayPath) {
        _displayPath = displayPath;
        return this;
    }

    public ChooserDialog build() {

        fsDialog = new FileDialogFragment();
        refreshDirs();

        fsDialog.setOnItemClickListener(v -> onItemClick((int) v.getTag()));
        fsDialog.setOnActionClickListener(v -> onActionClick((String) v.getTag()));

        return this;
    }

    public ChooserDialog show() {
        if (fsDialog == null) {
            build();
        }

        if (_permissionListener == null) {
            _permissionListener = new PermissionsUtil.OnPermissionListener() {
                @Override
                public void onPermissionGranted(String[] permissions) {
                    boolean show = false;
                    for (String permission : permissions) {
                        if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            show = true;
                            break;
                        }
                    }
                    if (!show) return;
                    if (_enableOptions) {
                        show = false;
                        for (String permission : permissions) {
                            if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                show = true;
                                break;
                            }
                        }
                    }
                    if (!show) return;
                    fsDialog.show(activity.getSupportFragmentManager(), "fileDialog");
                    if (fsDialog.getAdapter().getItemCount() <= 0) {
                        refreshDirs();
                    }
                }

                @Override
                public void onPermissionDenied(String[] permissions) {
                    //
                }

                @Override
                public void onShouldShowRequestPermissionRationale(final String[] permissions) {
                    Toast.makeText(_context, "You denied the Read/Write permissions on SDCard.",
                            Toast.LENGTH_LONG).show();
                }
            };
        }

        final String[] permissions = _enableOptions ? new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}
                : new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

       PermissionsUtil.checkPermissions(_context, _permissionListener, permissions);

        return this;
    }

    private String removableRoot = null;
    private String primaryRoot = null;

    private void listDirs() {
        _entries.clear();

        if (_currentDir == null) {
            _currentDir = new File(FileUtil.getStoragePath(_context, false));
        }

        // Get files
        File[] files = _currentDir.listFiles(_fileFilter);

        // Add the ".." entry
        boolean up = false;
        if (removableRoot == null || primaryRoot == null) {
            removableRoot = FileUtil.getStoragePath(_context, true);
            primaryRoot = FileUtil.getStoragePath(_context, false);
        }
        if (!removableRoot.equals(primaryRoot)) {
            if (_currentDir.getAbsolutePath().equals(primaryRoot)) {
                _entries.add(new RootFile(sSdcardStorage)); //⇠
                up = true;
            } else if (_currentDir.getAbsolutePath().equals(removableRoot)) {
                _entries.add(new RootFile(sPrimaryStorage)); //⇽
                up = true;
            }
        }
        if (!up && _currentDir.getParentFile() != null && _currentDir.getParentFile().canRead()) {
            _entries.add(new RootFile(".."));
        }

        if (files == null) return;

        List<File> dirList = new LinkedList<>();
        List<File> fileList = new LinkedList<>();

        for (File f : files) {
            if (f.isDirectory()) {
                dirList.add(f);
            } else {
                fileList.add(f);
            }
        }

        sortByName(dirList);
        sortByName(fileList);
        _entries.addAll(dirList);
        _entries.addAll(fileList);

        if (fsDialog != null && _displayPath) {
            fsDialog.displayPath(_currentDir.getPath());
        }
    }

    private void sortByName(List<File> list) {
        Collections.sort(list, (f1, f2) -> f1.getName().toLowerCase().compareTo(f2.getName().toLowerCase()));
    }

    private void onItemClick(int position) {
        if (position < 0 || position >= _entries.size()) return;

        File file = _entries.get(position);
        if (file.getName().equals("..")) {
            doGoBack();
            return;
        } else if (file.getName().contains(sSdcardStorage)) {
            if (removableRoot == null) {
                removableRoot = FileUtil.getStoragePath(_context, true);
            }
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                _currentDir = new File(removableRoot);
                _chooseMode = _chooseMode == CHOOSE_MODE_DELETE ? CHOOSE_MODE_NORMAL : _chooseMode;
            }
        } else if (file.getName().contains(sPrimaryStorage)) {
            if (primaryRoot == null) {
                primaryRoot = FileUtil.getStoragePath(_context, false);
            }
            _currentDir = new File(primaryRoot);
            _chooseMode = _chooseMode == CHOOSE_MODE_DELETE ? CHOOSE_MODE_NORMAL : _chooseMode;
        } else {

            if (file.isDirectory()) {
                if (_folderNavToCB == null) _folderNavToCB = _defaultNavToCB;
                if (_folderNavToCB.canNavigate(file)) {
                    _currentDir = file;
                }
            } else if ((!_dirOnly) && _result != null) {
                String path = file.getAbsolutePath();
                if (selected.containsKey(path)) {
                    selected.remove(path);
                    selectedIds.remove(Integer.valueOf(position));
                } else {
                    selected.put(path, file);
                    selectedIds.add(position);
                }
            }
        }
        refreshDirs();
    }

    private void send() {
        if (_result != null) {
            _result.onChoosePath(selected);
        }
    }

    private void onActionClick(String type) {

        switch (type) {
            case "send":
                send();

            case "cancel":
            default:
                fsDialog.dismiss();
        }
    }

    private void refreshDirs() {
        listDirs();
        fsDialog.setFiles(_entries, selectedIds);
    }

    private void doGoBack() {
        File f = _currentDir.getParentFile();
        if (_folderNavUpCB == null) _folderNavUpCB = _defaultNavUpCB;
        if (_folderNavUpCB.canUpTo(f)) {
            _currentDir = f;
            _chooseMode = _chooseMode == CHOOSE_MODE_DELETE ? CHOOSE_MODE_NORMAL : _chooseMode;
            refreshDirs();
        }
    }
}
