package com.bhyh.utils.sdk;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import com.bhyh.netsdk.common.Res;
import com.bhyh.netsdk.demo.frame.ThermalCamera.TemperQueryDialog.QUERY_TYPE;
import com.bhyh.netsdk.demo.frame.ThermalCamera.TemperQueryDialog.QueryShowPanel;
import com.bhyh.netsdk.demo.module.ThermalCameraModule;
import com.bhyh.netsdk.lib.ToolKits;
import com.bhyh.netsdk.lib.NetSDKLib.NET_IN_RADIOMETRY_STARTFIND;
import com.bhyh.netsdk.lib.NetSDKLib.NET_OUT_RADIOMETRY_DOFIND;

/**
 * 查找工作线程（完成异步搜索）
 */
public class BhQuerySwingWorker extends SwingWorker<NET_OUT_RADIOMETRY_DOFIND, Object> {
	
	private QUERY_TYPE type;
	private int offset = 0;
	
	private NET_IN_RADIOMETRY_STARTFIND stuStartFind = new NET_IN_RADIOMETRY_STARTFIND();
	
	public BhQuerySwingWorker(QUERY_TYPE type) {
		this.type = type;
	}
	
	protected NET_OUT_RADIOMETRY_DOFIND doInBackground() {

		int currentIndex = 0/*showPanel.getIndex()*/;
		try {
			switch(type) {
				case FIRST_PAGE_QUERY:
					ThermalCameraModule.stopFind();
					if (!ThermalCameraModule.startFind(stuStartFind)) {
						return null;
					}
					offset = 0;
					break;
				case PRE_PAGE_QUERY:
					offset = ((currentIndex-1)/QueryShowPanel.QUERY_SHOW_COUNT-1) * QueryShowPanel.QUERY_SHOW_COUNT;
					break;
				case NEXT_PAGE_QUERY:
					offset = currentIndex;
					break;
				default:
					break;
			}
			
			
			NET_OUT_RADIOMETRY_DOFIND stuDoFind = ThermalCameraModule.doFind(offset, QueryShowPanel.QUERY_SHOW_COUNT);

			return stuDoFind;
		}catch (Exception e) {
			System.out.println(" -------- doInBackground Exception -------- ");
		}
		return null;
	}
	
	@Override
	protected void done() {	
		
		try {

			NET_OUT_RADIOMETRY_DOFIND stuDoFind = get();
			if (stuDoFind == null) {
				JOptionPane.showMessageDialog(null, ToolKits.getErrorCodeShow(), Res.string().getErrorMessage(), JOptionPane.ERROR_MESSAGE);
				return;
			}
			
//			System.out.println("offset " + offset + " nFound " + stuDoFind.nFound + " Total " + ThermalCameraModule.getTotalCount());

			if (stuDoFind.nFound == 0) {
				JOptionPane.showMessageDialog(null, Res.string().getFailed(), Res.string().getErrorMessage(), JOptionPane.ERROR_MESSAGE);
				return;
			}
			
		//	showPanel.setIndex(offset);
		//	showPanel.insertData(stuDoFind);				
		} catch (Exception e) {
//			e.printStackTrace();
		}finally {
		//	setSearchEnable(true);
		}
	}
}
