package com.mmsstudio.htmldesignsample;

import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private SliderAdapter adapter;
    private LinearLayout dotsLayout;
    private View[] dots;
    private Handler autoSlideHandler = new Handler();

    // bubble views
    private View bubble1, bubble2, bubble3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // handle edge‑to‑edge padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets sys = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(sys.left, sys.top, sys.right, sys.bottom);
            return insets;
        });

        viewPager = findViewById(R.id.viewPager);
        dotsLayout = findViewById(R.id.dotsLayout);

        // find your bubble views
        bubble1 = findViewById(R.id.bubble1);
        bubble2 = findViewById(R.id.bubble2);
        bubble3 = findViewById(R.id.bubble3);

        // load and start each bubble animation
        Animation animPing      = AnimationUtils.loadAnimation(this, R.anim.anim_ping);
        Animation animPulseSlow = AnimationUtils.loadAnimation(this, R.anim.anim_pulse_slow);
        Animation animBounce    = AnimationUtils.loadAnimation(this, R.anim.anim_bounce);

        bubble1.startAnimation(animPing);
        bubble2.startAnimation(animPulseSlow);
        bubble3.startAnimation(animBounce);

        // 1) set up your slides
        adapter = new SliderAdapter(this, new SlideItem[]{
                new SlideItem(R.drawable.ic_graduation_cap, "Master English",
                        "Build your vocabulary with interactive lessons", R.anim.anim_pulse_slow),
                new SlideItem(R.drawable.ic_comments, "Practice Speaking",
                        "Improve pronunciation with audio features", R.anim.anim_float),
                new SlideItem(R.drawable.ic_brain, "AI Translation",
                        "Smart translation for complex sentences", R.anim.anim_pulse_slow)
        });
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(new ZoomOutPageTransformer());

        // 2) create & display the dots
        setupDots(adapter.getItemCount());

        // 3) update dots + reset auto‑slide on page change
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                highlightDot(position);
                autoSlideHandler.removeCallbacks(slideRunnable);
                autoSlideHandler.postDelayed(slideRunnable, 5000);
            }
        });

        // 4) start auto‑slide
        autoSlideHandler.postDelayed(slideRunnable, 5000);
    }

    /**
     * create N small circle Views and add them to dotsLayout
     */
    private void setupDots(int count) {
        dots = new View[count];
        dotsLayout.removeAllViews();

        for (int i = 0; i < count; i++) {
            View dot = new View(this);
            dot.setBackgroundResource(R.drawable.dot);  // see below
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    dpToPx(8), dpToPx(8)
            );
            lp.setMarginStart(dpToPx(4));
            lp.setMarginEnd(dpToPx(4));
            dot.setLayoutParams(lp);

            final int index = i;
            dot.setOnClickListener(v -> viewPager.setCurrentItem(index, true));

            dotsLayout.addView(dot);
            dots[i] = dot;
        }

        // highlight the first one initially
        highlightDot(0);
    }

    /**
     * set full opacity on the active dot, dim the rest
     */
    private void highlightDot(int activePosition) {
        for (int i = 0; i < dots.length; i++) {
            dots[i].setAlpha(i == activePosition ? 1f : 0.3f);
        }
    }

    /**
     * simulate 8 dp in pixels
     */
    private int dpToPx(int dp) {
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics()
        ));
    }

    /**
     * advances to next slide
     */
    private final Runnable slideRunnable = () -> {
        int next = (viewPager.getCurrentItem() + 1) % adapter.getItemCount();
        viewPager.setCurrentItem(next, true);
    };

    @Override
    protected void onPause() {
        super.onPause();
        autoSlideHandler.removeCallbacks(slideRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        autoSlideHandler.postDelayed(slideRunnable, 5000);
    }
}
