package com.bhyh.utils.sdk;

import com.bhyh.netsdk.lib.NetSDKLib;
import com.bhyh.netsdk.lib.NetSDKLib.LLong;
import com.sun.jna.Pointer;

public  class BhDisConnect implements NetSDKLib.fDisConnect {

	@Override
	public void invoke(LLong lLoginID, String pchDVRIP, int nDVRPort, Pointer dwUser) {
		System.out.printf("Device[%s] Port[%d] DisConnect!\n", pchDVRIP, nDVRPort);
		
	}

}
