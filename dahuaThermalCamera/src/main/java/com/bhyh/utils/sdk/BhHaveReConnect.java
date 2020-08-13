package com.bhyh.utils.sdk;

import com.bhyh.netsdk.lib.NetSDKLib;
import com.bhyh.netsdk.lib.NetSDKLib.LLong;
import com.sun.jna.Pointer;

public class BhHaveReConnect implements NetSDKLib.fHaveReConnect{

	@Override
	public void invoke(LLong lLoginID, String pchDVRIP, int nDVRPort, Pointer dwUser) {
		System.out.printf("ReConnect Device[%s] Port[%d]\n", pchDVRIP, nDVRPort);
		
	}

}
