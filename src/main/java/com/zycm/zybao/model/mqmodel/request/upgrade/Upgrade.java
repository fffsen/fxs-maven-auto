package com.zycm.zybao.model.mqmodel.request.upgrade;

import java.math.BigDecimal;

/** app升级消息体
* @ClassName: Upgrade
* @Description: TODO
* @author sy
* @date 2017年9月13日 上午11:54:31
*
*/
public class Upgrade {

	    private Integer materialId;

	    private String materialName;

	    private String materialPath;

	    private Integer type;//素材类型 6安装包

	    private BigDecimal size;

	    private String appVersion;//app版本

	    private Integer isSave;//是否需要app更新这个版本为当前的最新版本

		public Integer getMaterialId() {
			return materialId;
		}

		public void setMaterialId(Integer materialId) {
			this.materialId = materialId;
		}

		public String getMaterialName() {
			return materialName;
		}

		public void setMaterialName(String materialName) {
			this.materialName = materialName;
		}

		public String getMaterialPath() {
			return materialPath;
		}

		public void setMaterialPath(String materialPath) {
			this.materialPath = materialPath;
		}

		public Integer getType() {
			return type;
		}

		public void setType(Integer type) {
			this.type = type;
		}

		public BigDecimal getSize() {
			return size;
		}

		public void setSize(BigDecimal size) {
			this.size = size;
		}

		public String getAppVersion() {
			return appVersion;
		}

		public void setAppVersion(String appVersion) {
			this.appVersion = appVersion;
		}

		public Integer getIsSave() {
			return isSave;
		}

		public void setIsSave(Integer isSave) {
			this.isSave = isSave;
		}



}
