# StarRating
自定义星标评级
支持自定义图片与颜色,星星间距以及是否可以手动选择
```
<resources>
    <declare-styleable name="StarRatingBar">
        <!-- 默认的星星图片 优先级高于绘制的星星 -->
        <attr name="defaultStar" format="reference" />
        <!-- 选中的星星图片 -->
        <attr name="star" format="reference" />
        <!-- 默认的星星颜色 -->
        <attr name="defaultStarColor" format="color" />
        <!-- 选中的星星颜色 -->
        <attr name="starColor" format="color" />
        <!-- 星星的个数 -->
        <attr name="starNum" format="integer" />
        <!-- 可选的星星的步长 如：0.5表示可选半颗星 -->
        <attr name="starStep" format="float" />
        <!-- 星星之间的间距 -->
        <attr name="starGap" format="dimension" />
        <!-- 星星的大小 -->
        <attr name="starSize" format="dimension" />
        <!-- 选中星星的部分 小于等于starNum -->
        <attr name="rating" format="float" />
        <!-- 星星是否响应事件 默认为true 表示仅作为展示 -->
        <attr name="isIndicator" format="boolean" />
    </declare-styleable>
</resources>
```
![示例图](https://upload-images.jianshu.io/upload_images/5425523-4ba0c781d554b6c4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/360)
