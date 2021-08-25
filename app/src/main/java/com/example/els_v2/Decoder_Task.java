/*
 * MIT License
 *
 * Copyright (c) 2017 Yuriy Budiyev [yuriy.budiyev@yandex.ru]
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.example.els_v2;

import android.hardware.Camera;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;

final class Decoder_Task {
    private final byte[] mImage;
    private final Point_ mImageSize;
    private final Point_ mPreviewSize;
    private final Point_ mViewSize;
    private final Rect_ mViewFrameRect;
    private final int mOrientation;
    private final boolean mReverseHorizontal;
    public Camera mcamera;

    public Decoder_Task(@NonNull final byte[] image, @NonNull final Point_ imageSize,
                        @NonNull final Point_ previewSize, @NonNull final Point_ viewSize,
                        @NonNull final Rect_ viewFrameRect, final int orientation,
                        final boolean reverseHorizontal, Camera camera) {
        mImage = image;
        mImageSize = imageSize;
        mPreviewSize = previewSize;
        mViewSize = viewSize;
        mViewFrameRect = viewFrameRect;
        mOrientation = orientation;
        mReverseHorizontal = reverseHorizontal;
        mcamera = camera;

    }

    @Nullable
    @SuppressWarnings("SuspiciousNameCombination")
    public Result decode(@NonNull final Multi_FormatReader reader) throws ReaderException {
        int imageWidth = mImageSize.getX();
        int imageHeight = mImageSize.getY();
        final int orientation = mOrientation;
        final byte[] image = Utils_.rotateYuv(mImage, imageWidth, imageHeight, orientation);

        if (orientation == 90 || orientation == 270) {
            final int width = imageWidth;
            imageWidth = imageHeight;
            imageHeight = width;
        }
        final Rect_ frameRect =
                Utils_.getImageFrameRect(imageWidth, imageHeight, mViewFrameRect, mPreviewSize,
                        mViewSize);
        final int frameWidth = frameRect.getWidth();
        final int frameHeight = frameRect.getHeight();
        if (frameWidth < 1 || frameHeight < 1) {
            return null;
        }
        return Utils_.decodeLuminanceSource(reader,
                new PlanarYUVLuminanceSource(image, imageWidth, imageHeight, frameRect.getLeft(),
                        frameRect.getTop(), frameWidth, frameHeight, mReverseHorizontal), mcamera);
    }

}
