package kr.doublechain.basic.explorer.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * MetaDataVO
 *
 */
@Setter
@Getter
public class Meta {
	
	@ApiModelProperty(value = "total count", position = 1)
	private Integer total = 0;
	
	/** 전체 페이지 */
	@ApiModelProperty(value = "전체 페이지", position = 2)
	private Integer totalPages = 0;

	/** 페이징당 보여주는 건수 */
	@ApiModelProperty(value = "페이징당 보여주는 건수", position = 3)
	private Integer countPerPage = 0;
	
	/** link info */
	@ApiModelProperty(value = "link info", position = 4)
	private Link link = new Link(); 
	
	/** 현재 보고 있는 페이지 */
	@ApiModelProperty(value = "현재 보고 있는 페이지", position = 5)
	private Integer page = 1;
	
	/**
	 * inner class link
	 *
	 */
	@Setter
	@Getter
	@Accessors(chain = true)
	@ApiModel(description="Link Information")
	public static class Link {
		
		/** pagination previous */
		private String previous = "";

		/** pagination next */
		private String next = "";
		
	}

}
