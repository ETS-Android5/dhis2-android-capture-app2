package com.dhis2.usescases.qrCodes;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class QrAdapter extends FragmentStatePagerAdapter {

    @NonNull
    private final List<QrViewModel> bitmaps;

    QrAdapter(@NonNull FragmentManager fragmentManager, @NonNull List<QrViewModel> bitmaps) {
        super(fragmentManager);
        this.bitmaps = bitmaps;
    }

    @Override
    public Fragment getItem(int position) {
        return QrFragment.create(bitmaps.get(position).getQrBitmap());
    }

    @Override
    public int getCount() {
        return bitmaps.size();
    }
}