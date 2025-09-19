package com.nju;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class WatermarkEXIF {

    // 获取 EXIF 日期，如果没有则返回文件修改时间
    public static String getExifDate(File file) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            if (directory != null) {
                Date date = directory.getDateOriginal();
                if (date != null) {
                    return new SimpleDateFormat("yyyy-MM-dd").format(date);
                }
            }
        } catch (Exception ignored) {
        }
        // 如果没有 EXIF，就用文件修改日期
        Date lastModified = new Date(file.lastModified());
        return new SimpleDateFormat("yyyy-MM-dd").format(lastModified);
    }

    // 添加水印
    public static BufferedImage addWatermark(BufferedImage img, String text, int fontSize, Color color, String position) {
        Graphics2D g = img.createGraphics();
        g.setColor(color);
        g.setFont(new Font("Arial", Font.BOLD, fontSize));

        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();

        int x = 0, y = 0;
        switch (position.toLowerCase()) {
            case "top_left":
                x = 10;
                y = textHeight;
                break;
            case "top_right":
                x = img.getWidth() - textWidth - 10;
                y = textHeight;
                break;
            case "bottom_left":
                x = 10;
                y = img.getHeight() - 10;
                break;
            case "bottom_right":
                x = img.getWidth() - textWidth - 10;
                y = img.getHeight() - 10;
                break;
            case "center":
                x = (img.getWidth() - textWidth) / 2;
                y = (img.getHeight() + textHeight) / 2;
                break;
        }

        g.drawString(text, x, y);
        g.dispose();
        return img;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1. 输入路径
        System.out.print("请输入图片文件或目录路径: ");
        String path = scanner.nextLine().trim();
        File inputFile = new File(path);

        if (!inputFile.exists()) {
            System.out.println("路径不存在！");
            return;
        }

        // 2. 输入字体大小
        System.out.print("请输入字体大小 (默认30): ");
        String fontInput = scanner.nextLine().trim();
        int fontSize = fontInput.isEmpty() ? 30 : Integer.parseInt(fontInput);

        // 3. 输入颜色
        System.out.print("请输入颜色 (R,G,B 例如255,255,255，默认白色): ");
        String colorInput = scanner.nextLine().trim();
        Color color;
        if (colorInput.isEmpty()) {
            color = Color.WHITE;
        } else {
            String[] parts = colorInput.split(",");
            color = new Color(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
        }

        // 4. 输入位置
        System.out.print("请输入水印位置 (top_left, top_right, bottom_left, bottom_right, center，默认bottom_right): ");
        String position = scanner.nextLine().trim();
        if (position.isEmpty()) position = "bottom_right";

        // 5. 获取待处理文件列表
        File[] files;
        if (inputFile.isFile()) {
            files = new File[]{inputFile};
        } else if (inputFile.isDirectory()) {
            files = inputFile.listFiles(f -> {
                String name = f.getName().toLowerCase();
                return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png");
            });
        } else {
            System.out.println("路径无效！");
            return;
        }

        if (files == null || files.length == 0) {
            System.out.println("没有找到图片文件！");
            return;
        }

        // 6. 输出目录
        File outDir;
        File parentDir = inputFile.isFile() ? inputFile.getParentFile() : inputFile;
        outDir = new File(parentDir, parentDir.getName() + "_watermark");
        if (!outDir.exists()) outDir.mkdirs();

        // 7. 处理图片
        for (File f : files) {
            try {
                String dateText = getExifDate(f);
                BufferedImage img = ImageIO.read(f);
                BufferedImage watermarked = addWatermark(img, dateText, fontSize, color, position);

                String ext = f.getName().substring(f.getName().lastIndexOf(".") + 1);
                File outFile = new File(outDir, f.getName());
                ImageIO.write(watermarked, ext, outFile);

                System.out.println("保存: " + outFile.getAbsolutePath());
            } catch (Exception e) {
                System.out.println("处理失败: " + f.getName());
            }
        }

        System.out.println("全部完成！");
    }
}
