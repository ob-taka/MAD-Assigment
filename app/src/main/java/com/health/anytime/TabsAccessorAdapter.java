package com.health.anytime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
/*This adapter is to switch between chat and contact fragment in one activity/UI which is specified as */

public class TabsAccessorAdapter extends FragmentPagerAdapter
{
    public TabsAccessorAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case(0):
                ChatsFragment chatsFragment = new ChatsFragment();
                return chatsFragment;
            case(1):
                ContactsFragment contactsFragment = new ContactsFragment();
                return contactsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount()
    {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case(0):
                return "Chats";
            case(1):
                return "Contacts";
            default:
                return null;
        }
    }
}
