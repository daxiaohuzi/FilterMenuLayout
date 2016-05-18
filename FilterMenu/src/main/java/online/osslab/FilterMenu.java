package online.osslab;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import java.util.ArrayList;
import java.util.List;


/**
 * 过滤菜单
 * http://filtermenu.osslab.online
 */

public class FilterMenu implements IMenu {

    private List<Item> items = new ArrayList<>();
    private OnMenuChangeListener listener;
    private FilterMenuLayout layout;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public OnMenuChangeListener getListener() {
        return listener;
    }

    public void setListener(OnMenuChangeListener listener) {
        this.listener = listener;
        for (final Item item : getItems()) {
            item.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("FilterMenu", "onClick");
                    if (getListener() != null) {
                        getListener().onMenuItemClick(item.getView(), item.getPosition());
                    }
                    if (layout != null) {
                        layout.collapse(true);
                    }
                }
            });
        }
    }

    @Override
    public void collapse(boolean animate) {
        layout.collapse(animate);
    }

    @Override
    public void expand(boolean animate) {
        layout.expand(animate);
    }

    @Override
    public void toggle(boolean animate) {
        layout.toggle(animate);
    }

    @Override
    public void setMenuLayout(FilterMenuLayout view) {
        this.layout = view;
        if (view == null) {
            return;
        }
        for (final Item item : getItems()) {
            layout.addView(item.getView());
        }
        layout.setMenu(this);
    }

    public static interface OnMenuChangeListener {
        void onMenuItemClick(View view, int position);

        void onMenuCollapse();

        void onMenuExpand();
    }

    public static class Builder {
        OnMenuChangeListener listener;
        private List<Item> items = new ArrayList<>();
        private Context context;
        private LayoutInflater inflater;
        private FilterMenuLayout layout;

        public Builder(Context context) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
        }

        public Builder withListener(OnMenuChangeListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder inflate(int menuResId) {
            PopupMenu popupMenu = new PopupMenu(context, null);
            popupMenu.inflate(menuResId);
            Menu menu = popupMenu.getMenu();
            for (int i = 0; i < menu.size(); i++) {
                MenuItem item = menu.getItem(i);
                addItem(item.getIcon());
            }
            menu.clear();
            menu = null;
            popupMenu = null;
            return this;
        }

        public Builder addItem(Drawable icon) {
            ImageButton view = (ImageButton) inflater.inflate(R.layout.menu_item, null, false);
            view.setImageDrawable(icon);
            addItem(view);
            return this;
        }

        public Builder addItem(int iconResId) {
            Drawable icon = context.getResources().getDrawable(iconResId);
            addItem(icon);
            return this;
        }

        public Builder addItem(View view) {
            Item item = new Item();
            item.setView(view);
            item.setPosition(items.size());
            item.getView().setTag(item);
            items.add(item);

            return this;
        }

        public Builder attach(FilterMenuLayout view) {
            this.layout = view;
            return this;
        }

        public FilterMenu build() {
            FilterMenu menu = new FilterMenu();
            menu.setItems(items);
            menu.setListener(this.listener);
            menu.setMenuLayout(this.layout);
            return menu;
        }
    }

    public static class Item {
        private View view;
        private int x;
        private int y;
        private int position;
        private Rect bounds = new Rect(0, 0, 0, 0);

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
            view.setAlpha(0f);
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public void setBounds(int left, int top, int right, int bottom) {
            this.bounds.set(left, top, right, bottom);
        }

        public Rect getBounds() {
            return bounds;
        }

        public void setBounds(Rect bounds) {
            this.bounds = bounds;
        }
    }
}
