package tech.otter.merchant.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class MyPacker {
	public static void main (String[] args) throws Exception {
		TexturePacker.process("android/assets/unpacked/goods", "android/assets/images/", "goods");
		TexturePacker.process("android/assets/unpacked/merchants", "android/assets/images/", "merchants");
		TexturePacker.process("android/assets/unpacked/ui", "android/assets/images/", "ui");
	}
}