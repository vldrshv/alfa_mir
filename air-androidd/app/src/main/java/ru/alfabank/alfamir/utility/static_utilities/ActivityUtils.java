package ru.alfabank.alfamir.utility.static_utilities;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ru.alfabank.alfamir.R;

import static com.google.common.base.Preconditions.checkNotNull;
import static ru.alfabank.alfamir.Constants.FRAGMENT_TRANSITION_ADD;
import static ru.alfabank.alfamir.Constants.FRAGMENT_TRANSITION_ADD_TO_LEFT;
import static ru.alfabank.alfamir.Constants.FRAGMENT_TRANSITION_REPLACE;
import static ru.alfabank.alfamir.Constants.FRAGMENT_TRANSITION_REPLACE_FROM_RIGHT;
import static ru.alfabank.alfamir.Constants.FRAGMENT_TRANSITION_REPLACE_TO_LEFT;

public class ActivityUtils {

    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment,
                                             int frameId,
                                             int animationType,
                                             boolean isBackStacked) {
        addFragment(fragmentManager, fragment, frameId, animationType, isBackStacked);
    }

    private static void addFragment(@NonNull FragmentManager fragmentManager,
                                    @NonNull Fragment fragment,
                                    int frameId,
                                    int animationType,
                                    boolean isBackStacked){
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (animationType){
            case FRAGMENT_TRANSITION_ADD: {
                transaction.add(frameId, fragment, fragment.getClass().getName());
                if(isBackStacked) transaction.addToBackStack(fragment.getClass().getName());
                transaction.commit();
                break;
            }
            case FRAGMENT_TRANSITION_ADD_TO_LEFT:{
                transaction.setCustomAnimations(R.anim.slide_in_from_left, R.anim.slide_out_to_right, R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                if(isBackStacked) transaction.addToBackStack(fragment.getClass().getName());
                transaction.add(frameId, fragment, fragment.getClass().getName());
                transaction.commit();
                break;
            }
            case FRAGMENT_TRANSITION_REPLACE_FROM_RIGHT: {
                transaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                if(isBackStacked) transaction.addToBackStack(fragment.getClass().getName());
                transaction.replace(frameId, fragment, fragment.getClass().getName());
                transaction.commit();
                break;
            }
            case FRAGMENT_TRANSITION_REPLACE_TO_LEFT:{
                transaction.setCustomAnimations(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
                if(isBackStacked) transaction.addToBackStack(fragment.getClass().getName());
                transaction.replace(frameId, fragment, fragment.getClass().getName());
                transaction.commit();
                break;
            }
            case FRAGMENT_TRANSITION_REPLACE:{
                if(isBackStacked) transaction.addToBackStack(fragment.getClass().getName());
                transaction.replace(frameId, fragment, fragment.getClass().getName());
                transaction.commit();
                break;
            }
        }
    }

}
