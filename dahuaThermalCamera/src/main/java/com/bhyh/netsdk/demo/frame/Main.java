package com.bhyh.netsdk.demo.frame;

import javax.swing.SwingUtilities;

import com.bhyh.netsdk.common.SwitchLanguage;
import com.bhyh.netsdk.lib.NetSDKLib;


public class Main {  
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if(NetSDKLib.NETSDK_INSTANCE != null
					&& NetSDKLib.CONFIG_INSTANCE != null) {
					System.setProperty("java.awt.im.style", "on-the-spot"); // 去除中文输入弹出框
					SwitchLanguage demo = new SwitchLanguage();
					demo.setVisible(true);
				}
			}
		});	
	}
}