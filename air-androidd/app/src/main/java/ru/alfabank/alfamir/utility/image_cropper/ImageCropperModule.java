package ru.alfabank.alfamir.utility.image_cropper;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ImageCropperModule {

    @Binds
    abstract ImageCropper imageCropper (ImageCropperImp imageCropperImp);
}