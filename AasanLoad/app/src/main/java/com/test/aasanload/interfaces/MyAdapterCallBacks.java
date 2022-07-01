package com.test.aasanload.interfaces;


import com.test.aasanload.modelclasses.CodeModelClass;

import kotlin.Metadata;


public interface MyAdapterCallBacks {
    void copyItem(CodeModelClass codeModelClass);

    void itemSelected(CodeModelClass codeModelClass);
}
