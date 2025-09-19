# WatermarkEXIF

一个命令行 Java 程序，用于给图片添加基于 EXIF 拍摄日期的水印。

## ✨ 功能特点
- 读取单张图片或目录下所有图片的 **EXIF 拍摄日期**（年月日）。
- 在图片上自动添加日期水印。
- 用户可自定义：
  - 字体大小（默认：30）
  - 字体颜色（默认：白色）
  - 水印位置（左上角、居中、右下角等）
- 输出图片保存在输入目录的子目录：


## 📦 使用方法

1. **编译程序**
   ```bash
   javac -cp .:lib/metadata-extractor-2.18.0.jar src/com/nju/WatermarkEXIF.java
2. **运行程序**
   ```bash
   java -cp .:lib/metadata-extractor-2.18.0.jar src/com/nju/WatermarkEXIF
3. **程序交互示例**
   ```bash
    请输入图片文件或目录路径: image/shcool.jpg
    请输入字体大小 (默认30): 30
    请输入颜色 (默认WHITE): RED
    请输入位置 (LEFT_TOP, CENTER, RIGHT_BOTTOM，默认RIGHT_BOTTOM): LEFT_TOP

## 📂 项目结构示例
WatermarkEXIF/
├── src/
│   └── com/nju/WatermarkEXIF.java
├── lib/
│   └── metadata-extractor-2.18.0.jar
├── image/
│   └── shcool.jpg
└── README.md

## 📝 注意事项
- 如果图片没有 EXIF 拍摄日期，会显示 NO DATE。
- 支持 JPG、JPEG、PNG 等常见格式。
- 输出目录会自动创建，不会覆盖原始图片。
