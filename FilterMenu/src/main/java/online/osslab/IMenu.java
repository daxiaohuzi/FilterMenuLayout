package online.osslab;

/**
 * 过滤菜单
 * http://filtermenu.osslab.online
 */

public interface IMenu {
    void collapse(boolean animate);
    void expand(boolean animate);
    void toggle(boolean animate);
    void setMenuLayout(FilterMenuLayout layout);
}
