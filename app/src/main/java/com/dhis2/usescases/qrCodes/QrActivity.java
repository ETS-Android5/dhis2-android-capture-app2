package com.dhis2.usescases.qrCodes;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.dhis2.App;
import com.dhis2.R;
import com.dhis2.databinding.ActivityQrCodesBinding;
import com.dhis2.usescases.general.ActivityGlobalAbstract;

import java.util.List;

import javax.inject.Inject;

import static com.dhis2.data.qr.QRjson.ATTR_JSON;
import static com.dhis2.data.qr.QRjson.ENROLLMENT_JSON;
import static com.dhis2.data.qr.QRjson.EVENTS_JSON;
import static com.dhis2.data.qr.QRjson.TEI_JSON;

/**
 * QUADRAM. Created by ppajuelo on 21/06/2018.
 */

public class QrActivity extends ActivityGlobalAbstract implements QrContracts.View {

    @Inject
    public QrContracts.Presenter presenter;

    private ActivityQrCodesBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ((App) getApplicationContext()).userComponent().plus(new QrModule()).inject(this);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qr_codes);
        binding.setName(getString(R.string.share_qr));
        binding.setPresenter(presenter);
        String teiUid = getIntent().getStringExtra("TEI_UID");
        presenter.generateQrs(teiUid, this);
    }

    @Override
    public void showQR(@NonNull List<QrViewModel> bitmaps) {
        QrAdapter qrAdapter = new QrAdapter(getSupportFragmentManager(), bitmaps);
        binding.viewPager.setAdapter(qrAdapter);

        binding.setTitle(getString(R.string.qr_id));
        binding.page.setText("1/" + bitmaps.size());
        binding.prev.setVisibility(View.GONE);
        binding.next.setVisibility(View.VISIBLE);

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // unused
            }

            @Override
            public void onPageSelected(int position) {
                binding.page.setText(position + 1 + "/" + bitmaps.size());

                if (position + 1 == bitmaps.size()){
                    binding.next.setVisibility(View.GONE);
                }
                else {
                    binding.next.setVisibility(View.VISIBLE);
                }

                if (position == 0){
                    binding.prev.setVisibility(View.GONE);
                }
                else {
                    binding.prev.setVisibility(View.VISIBLE);
                }

                switch (bitmaps.get(position).getQrType()){
                    case TEI_JSON:
                        binding.setTitle(getString(R.string.qr_id));
                        break;
                    case ATTR_JSON:
                        binding.setTitle(getString(R.string.qr_attributes));
                        break;
                    case ENROLLMENT_JSON:
                        binding.setTitle(getString(R.string.qr_enrollment));
                        break;
                    case EVENTS_JSON:
                        binding.setTitle(getString(R.string.qr_events));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // unused
            }
        });
    }

    @Override
    public void onBackClick() {
        super.onBackPressed();
    }

    @Override
    public void onPrevQr() {
        binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() - 1);
    }

    @Override
    public void onNextQr() {
        binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1);
    }
}
