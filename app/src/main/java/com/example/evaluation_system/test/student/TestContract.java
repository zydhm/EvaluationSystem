package com.example.evaluation_system.test.student;


public interface TestContract {

    interface View {
        /**
         *当列表为空时，显示空页面
         */
//        void showEmptyView();

        /**
         * 当加载更多数据时，显示加载页面
         */
//        void showLoadMoreView();

        /**
         * 隐藏加载更多界面
         */
//        void hideLoadMore();

        /**
         * 没有更多数据时显示
         */
//        void showNoMoreView();
    }

    interface Presenter {
        /**
         * 当界面切换到此fragment中时，初次请求数据
         */
        void getInitData(Integer studentId);


        /**
         * 判断数据服务器端是否还有数据，包含两种，全部用户或者朋友，具体判断由presenter完成
         * @return 还有则返回true，没有则返回false;
         */
        boolean canLoadMore();

        /**
         * 上拉加载更多数据
         */
        void loadMore();


        /**
         * 从服务器获取未完成的测试，每次10条
         */
        void getListFromServer(Integer studentId);

    }
}
