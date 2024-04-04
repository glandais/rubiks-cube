package io.github.glandais.rubikscube;

import io.github.glandais.rubikscube.model.FaceletEnum;
import io.github.glandais.rubikscube.model.SideEnum;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedString;

public class GenerateTextures {

    public static void main(String[] args) throws IOException {
        for (FaceletEnum faceletEnum : FaceletEnum.values()) {
            int size = 256;
            BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.getGraphics();
            javafx.scene.paint.Color color = faceletEnum.getSide().getColor();
            g.setColor(new Color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue()));
            g.fillRect(0, 0, size, size);

            String text = faceletEnum.name();

            Font font = new Font("Arial", Font.BOLD, 200);

            AttributedString attributedText = new AttributedString(text);
            attributedText.addAttribute(TextAttribute.FONT, font);
            attributedText.addAttribute(TextAttribute.FOREGROUND, Color.BLACK);

            FontMetrics metrics = g.getFontMetrics(font);
            int positionX = (image.getWidth() - metrics.stringWidth(text)) / 2;
            int positionY = (image.getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();

            g.drawString(attributedText.getIterator(), positionX, positionY);

            if (faceletEnum.getSide() == SideEnum.F ||
                faceletEnum.getSide() == SideEnum.L ||
                faceletEnum.getSide() == SideEnum.B ||
                faceletEnum.getSide() == SideEnum.R) {
                BufferedImage rotated = new BufferedImage(size, size, image.getType());
                for (int y = 0; y < size; y++) {
                    for (int x = 0; x < size; x++) {
                        rotated.setRGB((size - 1) - x, (size - 1) - y, image.getRGB(x, y));
                    }
                }

                image = rotated;
            }

            ImageIO.write(image, "png", new File("src/main/resources/textures/" + faceletEnum.name() + ".png"));
        }
    }

}
