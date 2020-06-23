package com.example.androidapp.adapter;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.androidapp.fragment.QueryResult.Apply;
import com.example.androidapp.fragment.QueryResult.Recruit;
import com.example.androidapp.fragment.QueryResult.Student;
import com.example.androidapp.fragment.QueryResult.Teacher;

import org.jetbrains.annotations.NotNull;

/**
 * 申请意向列表适配器
 */
public class QueryResultPageAdapter extends FragmentStatePagerAdapter {
    SparseArray<Fragment> registeredFragments = new SparseArray<>();
    int mNumOfTabs;

    public QueryResultPageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Teacher();
            case 1:
                return new Student();
            case 2:
                return new Recruit();
            case 3:
                return new Apply();
            default:
                return null;
        }
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @NotNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

}
