package com.bb.ringtopreddit.di;

import com.bb.ringtopreddit.picture.PictureFragmentModule;
import com.bb.ringtopreddit.top.TopFragmentModule;

import dagger.Module;

@Module(includes = {
        PictureFragmentModule.class
})
public class FragmentModules {
}
