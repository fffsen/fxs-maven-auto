package com.zycm.zybao.model.mqmodel.request.publish;

import com.zycm.zybao.service.facade.ResourceLibServiceFacade;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;

/** 节目素材信息消息体
* @ClassName: ProgramFileMsg
* @Description: TODO
* @author sy
* @date 2017年9月13日 上午9:49:11
*
*/
public class ProgramFileMsg implements Serializable{

	 	private Integer materialId;

	    private String materialName;

	    private BigDecimal size;

	    private String materialPath;

	    private Integer type;

	    private BigDecimal height;

	    private BigDecimal width;

	    private String sourceUrl;

	    private String effectiveTime;

	    private Integer materialOrder;

	    private Integer playTime;

	    private String extend;

	    private String checkCode;

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

		public BigDecimal getSize() {
			return size;
		}

		public void setSize(BigDecimal size) {
			this.size = size;
		}

		public String getMaterialPath() {
			if("oss".equals(ResourceLibServiceFacade.FTP_MODEL)){
				if(StringUtils.isNotBlank(materialPath) && "/".equals(materialPath.substring(0, 1))){
					materialPath = materialPath.substring(1);
				}
			}
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

		public BigDecimal getHeight() {
			return height;
		}

		public void setHeight(BigDecimal height) {
			this.height = height;
		}

		public BigDecimal getWidth() {
			return width;
		}

		public void setWidth(BigDecimal width) {
			this.width = width;
		}

		public String getSourceUrl() {
			return sourceUrl;
		}

		public void setSourceUrl(String sourceUrl) {
			this.sourceUrl = sourceUrl;
		}

		public String getEffectiveTime() {
			return effectiveTime;
		}

		public void setEffectiveTime(String effectiveTime) {
			this.effectiveTime = effectiveTime;
		}

		public Integer getMaterialOrder() {
			return materialOrder;
		}

		public void setMaterialOrder(Integer materialOrder) {
			this.materialOrder = materialOrder;
		}

		public Integer getPlayTime() {
			return playTime;
		}

		public void setPlayTime(Integer playTime) {
			this.playTime = playTime;
		}

		public String getExtend() {
			return extend;
		}

		public void setExtend(String extend) {
			this.extend = extend;
		}

		public String getCheckCode() {
			return checkCode;
		}

		public void setCheckCode(String checkCode) {
			this.checkCode = checkCode;
		}


}
