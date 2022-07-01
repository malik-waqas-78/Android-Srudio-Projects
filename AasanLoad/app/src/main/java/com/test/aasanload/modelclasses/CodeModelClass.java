package com.test.aasanload.modelclasses;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;



public final class CodeModelClass {
    private String code;
    private String copy;
    private boolean selected;

    public CodeModelClass() {
        String str = "";
        this.code = str;
        this.copy = str;
    }

    public final boolean getSelected() {
        return this.selected;
    }

    public final void setSelected(boolean z) {
        this.selected = z;
    }

    public final String getCode() {
        return this.code;
    }

    public final void setCode(String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.code = str;
    }

    public final String getCopy() {
        return this.copy;
    }

    public final void setCopy(String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.copy = str;
    }
}
