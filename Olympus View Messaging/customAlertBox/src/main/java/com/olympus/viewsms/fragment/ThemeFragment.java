package com.olympus.viewsms.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.olympus.viewsms.PreviewActivity;
import com.olympus.viewsms.R;
import com.olympus.viewsms.adapter.ThemeAdapter;
import com.olympus.viewsms.adapter.ThemeAdapter.OnClickBuy;
import com.olympus.viewsms.adapter.ThemeAdapter.OnClickPreview;
import com.olympus.viewsms.adapter.ThemeAdapter.OnClickUnlockFree;
import com.olympus.viewsms.adapter.TopThemeAdapter;
import com.olympus.viewsms.data.ThemeDAO;
import com.olympus.viewsms.model.Theme;
import com.olympus.viewsms.util.Constants;
import com.olympus.viewsms.util.Scale;

import java.util.ArrayList;
import java.util.List;

public final class ThemeFragment extends Fragment implements OnPageChangeListener {
    private MoreThemeFragment moreThemeFragment;

    private View view;
    private List<Theme> themes;
    private int theme_type;
    public  ThemeAdapter adapter;
    private RecyclerView recyclerView;

    private TopThemeAdapter adapter_pager;
    private ViewPager child_pager;
    private ImageView ivdot1;
    private ImageView ivdot2;
    private ImageView ivdot3;
    private ImageView ivdot4;
    private ImageView ivdot5;
    private int swipeListStatus=2;
    private  LinearLayoutManager layoutManager=null;
    private int lastVisiblePosition=0;
    private boolean hasMoved=false;
    private float startYPosition=0;
    private int lastAnimationStatus =-1;

    public static ThemeFragment newInstance(MoreThemeFragment moreThemeFragment, int theme_type) {
        ThemeFragment fragment = new ThemeFragment();

        if (fragment.moreThemeFragment == null) {
            fragment.moreThemeFragment = moreThemeFragment;
            fragment.theme_type = theme_type;
        }

        fragment.themes = new ArrayList<Theme>();

        // Swaping Game Themes and Unique Themes
        if (theme_type == 0) {
            theme_type = 2;
        } else if (theme_type == 2) {
            theme_type = 0;
        }
        for (Theme theme : moreThemeFragment.themes) {
            if (theme.getTheme_type() == theme_type)
                fragment.themes.add(theme);
        }

        return fragment;
    }

    public void recycleAdapterBitmaps() {
        if (adapter != null) {
            adapter.recycleBitmaps();
            System.gc();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recycleAdapterBitmaps();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_moretheme, container, false);
        if (themes != null) {
            adapter = new ThemeAdapter(getActivity(), ThemeAdapter.MODE_MORE, themes);

            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView1);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

            if(theme_type !=0){
                RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                recyclerView.setLayoutParams(params);
            }

            adapter.setOnClickBuyListener(new OnClickBuy() {
                @Override
                public void onClickBuy(int pos) {
                    clickUnLockTheme(themes.get(pos));
                }
            });
            adapter.setOnClickPreviewListener(new OnClickPreview() {
                @Override
                public void onClickPreview(int pos) {
                    Intent i = new Intent(getActivity(), PreviewActivity.class);
                    i.putExtra("theme_id", themes.get(pos).getId());
                    i.putExtra("avartar_id", pos % Constants.TOTAL_DEMO_AVARTARS);
                    startActivity(i);
                }
            });
            adapter.setOnClickUnlockFreeListener(new OnClickUnlockFree() {

                @Override
                public void onClickUnlockFree(int pos) {

                    moreThemeFragment.getTheme(themes.get(pos));
                    MoreThemeFragment.themes.remove(themes.get(pos));
                    MyThemeFragment.themes.add(themes.get(pos));
                    themes.remove(pos);

                    adapter.notifyItemRemoved(pos);
                    adapter.notifyItemRangeChanged(pos, themes.size());

                }
            });


            //	    } catch (InflateException e) {}

            recyclerView.setOnTouchListener(new OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {

                    if(theme_type==0 && layoutManager.findFirstVisibleItemPosition()==0){

                        if(event.getAction()==MotionEvent.ACTION_MOVE){
                            if(!hasMoved) {
                                hasMoved = true;
                                startYPosition=event.getY();
                                Log.d("AnimateDebug", "Move Y=" + event.getY());
                            }
                        }else if(event.getAction()==MotionEvent.ACTION_UP){
                            if(hasMoved){
                                Log.d("AnimateDebug","Up Y="+event.getY() );
                                float diff=event.getY()-startYPosition;
                                if(Math.abs(diff)>30) {
                                    hasMoved=false;

                                    Log.d("AnimateDebug"," lastAnimationStatus="+lastAnimationStatus +" diff="+diff);
                                    if (diff > 0 && lastAnimationStatus==1) {
                                        animateListDown();
                                        return true;
                                    } else if(diff<0 && lastAnimationStatus!=1){
                                        animateListUp();
                                        return true;
                                    }

                                }
                            }

                            startYPosition=0;
                            hasMoved=false;
                        }

                    }
                   return false;
                }
            });

            if (theme_type == 0) {
                ((LinearLayout) view.findViewById(R.id.ln_childpager)).setVisibility(View.VISIBLE);
                child_pager = (ViewPager) view.findViewById(R.id.child_pager);
                ivdot1 = (ImageView) view.findViewById(R.id.iv_dot1);
                ivdot2 = (ImageView) view.findViewById(R.id.iv_dot2);
                ivdot3 = (ImageView) view.findViewById(R.id.iv_dot3);
                ivdot4 = (ImageView) view.findViewById(R.id.iv_dot4);
                ivdot5 = (ImageView) view.findViewById(R.id.iv_dot5);

                adapter_pager = new TopThemeAdapter(getActivity(), this);
                child_pager.setAdapter(adapter_pager);
                child_pager.setCurrentItem(0);
                child_pager.setOnPageChangeListener(this);
            } else {
                ((LinearLayout) view.findViewById(R.id.ln_childpager)).setVisibility(View.GONE);
            }

           // setListViewHeightBasedOnChildren(lv);
        } else {
            Toast.makeText(getActivity(), "Themes not loaded", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void clickUnLockTheme(Theme buying_theme) {
        moreThemeFragment.getTheme(buying_theme);
    }
//	private void clickBuyTheme(Theme buying_theme){
//		if(buying_theme.getPrice()==0)  //free theme, just get
//		{
//			moreThemeFragment.getTheme(buying_theme);
//		}
//		else                     //paid theme, please buy
//		{
//			try{
//				if (!moreThemeFragment.mHelper.subscriptionsSupported() && !moreThemeFragment.in_app_enable) {
//					Utils.alert(getActivity(), getString(R.string.inapp_cant_start));
//					return;
//				}
//
//				//setWaitScreen(true);
//				moreThemeFragment.launchPurchaseFlow(buying_theme);
//			}
//			catch(Exception ex){
//				Utils.alert(getActivity(), getString(R.string.inapp_cant_start));
//				ex.printStackTrace();
//			}
//		}
//	}

    public void clickBuyTheme(int theme_id) {
        ThemeDAO themeDAO = new ThemeDAO(getActivity());
        Theme buying_theme = themeDAO.getByID(theme_id);
        //clickBuyTheme(buying_theme);
        clickUnLockTheme(buying_theme);
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int arg0) {
        ivdot1.setImageResource(R.drawable.dot_white);
        ivdot2.setImageResource(R.drawable.dot_white);
        ivdot3.setImageResource(R.drawable.dot_white);
        ivdot4.setImageResource(R.drawable.dot_white);
        ivdot5.setImageResource(R.drawable.dot_white);
        if (child_pager.getCurrentItem() == 0) {
            ivdot1.setImageResource(R.drawable.dot_green);
        } else if (child_pager.getCurrentItem() == 1) {
            ivdot2.setImageResource(R.drawable.dot_green);
        } else if (child_pager.getCurrentItem() == 2) {
            ivdot3.setImageResource(R.drawable.dot_green);
        } else if (child_pager.getCurrentItem() == 3) {
            ivdot4.setImageResource(R.drawable.dot_green);
        } else {
            ivdot5.setImageResource(R.drawable.dot_green);
        }
    }


    private void animateListUp(){
        lastAnimationStatus=1;
        Log.d("AnimateDebug"," up animation");
        TranslateAnimation animationUp=new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                0.0f, Animation.RELATIVE_TO_PARENT, -0.5f);
        animationUp.setDuration(1000);
        animationUp.setAnimationListener(animationListener);
        recyclerView.startAnimation(animationUp);
    }
    private void animateListDown(){
        lastAnimationStatus=2;
        ((LinearLayout) view.findViewById(R.id.ln_childpager)).setVisibility(View.VISIBLE);
        Log.d("AnimateDebug"," down animation");
        TranslateAnimation animationDown=new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,-0.5f, Animation.RELATIVE_TO_PARENT, 0.0f);
        animationDown.setDuration(1000);
        animationDown.setAnimationListener(animationListener);
        recyclerView.startAnimation(animationDown);
    }
    private Animation.AnimationListener animationListener=new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
                if(lastAnimationStatus==1){
                    RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                    recyclerView.setLayoutParams(params);
                    ((LinearLayout) view.findViewById(R.id.ln_childpager)).setVisibility(View.GONE);
                }else if(lastAnimationStatus==2){
                    RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, Scale.cvDPtoPX(getActivity(), 240),0,0);
                    recyclerView.setLayoutParams(params);
                }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

}
