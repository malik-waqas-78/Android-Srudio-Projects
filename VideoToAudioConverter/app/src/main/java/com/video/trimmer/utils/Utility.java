//  The MIT License (MIT)

//  Copyright (c) 2018 Intuz Pvt Ltd.

//  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files
//  (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify,
//  merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:

//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
//  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
//  LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
//  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.video.trimmer.utils;

public class Utility {

    //audio format in which file after trim will be saved.
    public static final String AUDIO_FORMAT = ".mp3";

    //audio mime type in which file after trim will be saved.
    public static final String AUDIO_MIME_TYPE = "audio/mp3";

    public static long getCurrentTime() {
        return System.nanoTime() / 1000000;
    }

}
