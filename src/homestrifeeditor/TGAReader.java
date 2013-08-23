/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Darlos9D
 */
public class TGAReader {
    private static byte ATTRIBUTE_BITS = 0x0F; //used to get the first 4 bits of a byte
    private static byte RIGHT_ALIGN = 0x10; //used to check the left/right alignment of the image pixels
    private static byte TOP_ALIGN = 0x20; //used to check the top/bottom alignment of the image pixels
    private static byte RL_PACKET = (byte)0x80; //used to see if an RLE packet is run-length or raw
    private static byte REP_COUNT_BITS = 0x7F; //used to get the first 7 bits of a byte
    
    public static int indexArrayToIndex(byte[] indexArray, boolean bigEndian)
    {
        if(bigEndian)
        {
            return indexArray[indexArray.length - 1] & 0xFF;
        }
        else
        {
            return indexArray[0] & 0xFF;
        }
    }
    
    public static short byteArrayToShort(byte[] paRawBytes, boolean pbBigEndian)
    {
        int iRetVal = -1;

        if(paRawBytes.length != 2)
        {
            return -1;
        }

        int iLow;
        int iHigh;

        if(pbBigEndian)
        {
            iLow  = paRawBytes[1];
            iHigh = paRawBytes[0];
        }
        else
        {
            iLow  = paRawBytes[0];
            iHigh = paRawBytes[1];
        }

        // Merge high-order and low-order byte to form a 16-bit double value.
        iRetVal = (iHigh << 8) | (0xFF & iLow);

        return (short)iRetVal;
    }
    
    public static ImageIcon loadTGA(String texFilePath, String palFilePath)
    {
        boolean bigEndian = false;
        
        byte[] imageWidthBytes = new byte[2];
        short imageWidth = 0;
        byte[] imageHeightBytes = new byte[2];
        short imageHeight = 0;
        BufferedImage image = null;
        
        FileInputStream file;
        FileInputStream palFile;
        byte[] palData = new byte[1];
        short fileBytes;
        int curFileByte;
        byte[] indexBytes;
	short index;
	boolean opaque = true;
	byte imageIDLength;
	byte colorMapType;
	byte imageType;
        byte[] colorMapLengthBytes = new byte[2];
	short colorMapLength;
	byte colorMapEntrySize;
	byte pixelDepth;
	byte bytesPerPixel;
	byte colorBytesPerPixel = 3; //BGR
	byte bytesPerColorMapEntry;
	byte GLbytesPerPixel = 4; //BGRA
	byte imageDescriptor;
	boolean topAlign = true;
	boolean rightAlign = true;
        
        try
        {
            file = new FileInputStream(texFilePath);
            
            //gather all the general info about the tga file
            imageIDLength = (byte)file.read(); //get the length of the image id field
            colorMapType = (byte)file.read(); //see if the file has a color map in it or not
            imageType = (byte)file.read(); //find out what kind of image file this is
            if(imageType != 9 && imageType != 10)
            {
            	file.close();
                return null; //this needs to be RLE, either indexed or truecolor
            }
        	boolean useInternalPalette = imageType == 9 && palFilePath.isEmpty();
        	/*
            if(imageType == 9 && palFilePath.isEmpty())
            {
                return null; //an indexed texture requires a palette
            }
            */
            file.skip(2); //skip the first two bytes of the color map specification
            file.read(colorMapLengthBytes); colorMapLength = byteArrayToShort(colorMapLengthBytes, bigEndian); //get the length of the color map
            colorMapEntrySize = (byte)file.read(); //get the size of each color map entry
            if(colorMapEntrySize == 15)
            {
                colorMapEntrySize = 16; //make sure it's a multiple of 8
            }
            bytesPerColorMapEntry = (byte)((int)colorMapEntrySize / 8); //get the size of each color map entry in bytes
            file.skip(4); //skip the first four bytes of the image specification
            file.read(imageWidthBytes); imageWidth = byteArrayToShort(imageWidthBytes, bigEndian); //get the image width
            file.read(imageHeightBytes); imageHeight = byteArrayToShort(imageHeightBytes, bigEndian); //get the image height
            pixelDepth = (byte)file.read(); //get the size of each pixel in bits
            bytesPerPixel = (byte)((int)pixelDepth/8); //get the size of each pixel in bytes
            if((imageType == 9 && pixelDepth != 8) || (imageType == 10 && pixelDepth != 24))
            {
            	file.close();
                return null; //only allowed configurations are: index with 8-bit, or truecolor with 24 bit
            }
            imageDescriptor = (byte)file.read(); //get the image descriptor, and pull some data from it
            if((imageDescriptor & ATTRIBUTE_BITS) > 0)
            {
            	file.close();
                return null; //no attributes should be defined
            }
            if((imageDescriptor & TOP_ALIGN) == 0)
            {
                topAlign = false; //the pixels are bottom-aligned
            }
            if((imageDescriptor & RIGHT_ALIGN) == 0)
            {
                rightAlign = false; //the pixels are left-aligned
            }
            file.skip(imageIDLength); //skip the image ID
            
            //okay, time for the fun part: picking through the image data.
            //it's ALWAYS going to be in RLE format so we can't just grab the raw data.
            //we need to take the compressed data and turn it into a 32bit BGRA format to pass to opengl
            int maxPixels = imageWidth * imageHeight; //we need this so we know when to stop
            int curPixels = 0; //keeps track of how far we've gone through the actual output buffer
            image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB); //set up our image data buffer
            byte repCount; //this'll hold each repetition count field
            byte[] color = new byte[colorBytesPerPixel]; //create a buffer to hold color data before it gets passed to the imageData
            Color colorObj = null;
            indexBytes = new byte[bytesPerPixel];
            if(imageType == 9 && !useInternalPalette)
            {
                file.skip(colorMapLength * bytesPerColorMapEntry); //skip the color map data
                palFile = new FileInputStream(palFilePath); //open the palette file, if this is an indexed image
                palData = new byte[palFile.available()];
                palFile.read(palData);
                palFile.close();
            }
            else if(useInternalPalette && colorMapType != 0) {
            	palData = new byte[colorMapLength * bytesPerColorMapEntry];
            	file.read(palData);
            }
            else if(imageType == 9) {
            	//We need palettes
            	file.close();
            	return null;
            }
            else {
            	//?
            }
            
            while(curPixels < maxPixels)
            {
                repCount = (byte)file.read(); //get the repetition count field
                if((repCount & RL_PACKET) == 0)
                {
                    //this is a raw packet.
                    //just shove the pixel data into the image data buffer
                    repCount = (byte)(repCount & REP_COUNT_BITS);
                    //put the pixels into the image data buffer
                    for(int i = 0; i <= repCount; i++)
                    {
                        //get the color data for this pixel
                        if(imageType == 9)
                        {
                            //the data in the file represents indicies, so get the proper color from the palette file
                            file.read(indexBytes); index = (short)indexArrayToIndex(indexBytes, bigEndian); // get the index
                            int alpha = index == 0 ? 0x00 : 0xFF;
                            colorObj = new Color
                            (
                                palData[(index * colorBytesPerPixel) + 2] & 0xFF,
                                palData[(index * colorBytesPerPixel) + 1] & 0xFF,
                                palData[(index * colorBytesPerPixel) + 0] & 0xFF,
                                alpha
                            );
                            int curRow = (maxPixels / imageWidth) - (curPixels / imageWidth) - 1;
                            int curPixelThisRow = curPixels % imageWidth;
                            image.setRGB(curPixelThisRow, curRow, colorObj.getRGB());
                        }
                        else if(imageType == 10)
                        {
                            //the data in the file represents the actual color, so just pull it directly into the buffer
                            file.read(color);
                            int alpha = color[0] == 255 && color[1] == 0 && color[2] == 255 ? 0x00 : 0xFF;
                            colorObj = new Color
                            (
                                color[2] & 0xFF,
                                color[1] & 0xFF,
                                color[0] & 0xFF,
                                alpha
                            );
                            int curRow = (maxPixels / imageWidth) - (curPixels / imageWidth) - 1;
                            int curPixelThisRow = curPixels % imageWidth;
                            image.setRGB(curPixelThisRow, curRow, colorObj.getRGB());
                        }
                    }
                }
                else
                {
                    //this is a run-length packet.
                    //Put the specified number of pixels of the specified color into the image data buffer
                    repCount = (byte)(repCount & REP_COUNT_BITS);
                    //get the color of this run
                    if(imageType == 9)
                    {
                        //the data in the file represents indicies, so get the proper color from the palette file
                        file.read(indexBytes); index = (short)indexArrayToIndex(indexBytes, bigEndian); //get the index
                        int alpha = index == 0 ? 0x00 : 0xFF;
                        colorObj = new Color
                        (
                            palData[(index * colorBytesPerPixel) + 2] & 0xFF,
                            palData[(index * colorBytesPerPixel) + 1] & 0xFF,
                            palData[(index * colorBytesPerPixel) + 0] & 0xFF,
                            alpha
                        );
                    }
                    else if(imageType == 10)
                    {
                        //the data in the file represents the actual color, so just pull it directly into the buffer
                        file.read(color); file.skip(bytesPerPixel - colorBytesPerPixel);
                        int alpha = color[0] == 255 && color[1] == 0 && color[2] == 255 ? 0x00 : 0xFF;
                        colorObj = new Color
                        (
                            color[2] & 0xFF,
                            color[1] & 0xFF,
                            color[0] & 0xFF,
                            alpha
                        );
                    }
                    for(int i = 0; i <= repCount; i++)
                    {
                        //put a pixel of the specified packet color into the image data buffer
                        int curRow = (maxPixels / imageWidth) - (curPixels / imageWidth) - 1;
                        int curPixelThisRow = (curPixels + i) % imageWidth;
                        image.setRGB(curPixelThisRow, curRow, colorObj.getRGB());
                    }
                }
                curPixels += repCount + 1;
            }
            
            file.close();
        }
        catch(FileNotFoundException e)
        {
        	JOptionPane.showMessageDialog(null, e.getMessage(), "File Not Found Exception", JOptionPane.ERROR_MESSAGE);   
            return null;
        }
        catch(IOException e)
        {
        	JOptionPane.showMessageDialog(null, e.getMessage(), "IO Exception", JOptionPane.ERROR_MESSAGE);               
        }
        
        ImageIcon icon = new ImageIcon(image);
        
        return icon;
    }
}
