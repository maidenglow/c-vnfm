package com.eluon.vepc.mano.vo;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class ScaleGroupVO extends BaseVO {
	@JsonProperty("group_no")
	private String group_no;
	@JsonProperty("group_name")
	private String group_name;
	
	@JsonProperty("in_no")
	private String in_no;
	@JsonProperty("in_cpu")
	private int in_cpu;
	@JsonProperty("in_memory")
	private int in_memory;
	@JsonProperty("in_cps")
	private int in_cps;
	
	@JsonProperty("out_no")
	private String out_no;
	@JsonProperty("out_cpu")
	private int out_cpu;
	@JsonProperty("out_memory")
	private int out_memory;
	@JsonProperty("out_cps")
	private int out_cps;

	@JsonProperty("create_no")
	private String create_no;
	@JsonProperty("img_id")
	private String img_id;
	@JsonProperty("img_name")
	private String img_name;
	@JsonProperty("flavor_id")
	private String flavor_id;
	@JsonProperty("flavor_name")
	private String flavor_name;
	@JsonProperty("keypair_name")
	private String keypair_name;
	
	@JsonProperty("network_info")
	private List<ScaleNetworkRefItemVO> network_info;
	@JsonProperty("instance_name")
	private List<String> instance_name;
	

	public String getGroup_no() {
		return group_no;
	}
	public void setGroup_no(String group_no) {
		this.group_no = group_no;
	}
	
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	
	public String getIn_no() {
		return in_no;
	}
	public void setIn_no(String in_no) {
		this.in_no = in_no;
	}
	
	public int getIn_cpu() {
		return in_cpu;
	}
	public void setIn_cpu(int in_cpu) {
		this.in_cpu = in_cpu;
	}
	
	public int getIn_memory() {
		return in_memory;
	}
	public void setIn_memory(int in_memory) {
		this.in_memory = in_memory;
	}
	
	public int getIn_cps() {
		return in_cps;
	}
	public void setIn_cps(int in_cps) {
		this.in_cps = in_cps;
	}
	
	public String getOut_no() {
		return out_no;
	}
	public void setOut_no(String out_no) {
		this.out_no = out_no;
	}
	
	public int getOut_cpu() {
		return out_cpu;
	}
	public void setOut_cpu(int out_cpu) {
		this.out_cpu = out_cpu;
	}
	
	public int getOut_memory() {
		return out_memory;
	}
	public void setOut_memory(int out_memory) {
		this.out_memory = out_memory;
	}
	
	public int getOut_cps() {
		return out_cps;
	}
	public void setOut_cps(int out_cps) {
		this.out_cps = out_cps;
	}
	
	public String getCreate_no() {
		return create_no;
	}
	public void setCreate_no(String create_no) {
		this.create_no = create_no;
	}
	
	public String getImg_id() {
		return img_id;
	}
	public void setImg_id(String img_id) {
		this.img_id = img_id;
	}
	
	public String getImg_name() {
		return img_name;
	}
	public void setImg_name(String img_name) {
		this.img_name = img_name;
	}
	
	public String getFlavor_id() {
		return flavor_id;
	}
	public void setFlavor_id(String flavor_id) {
		this.flavor_id = flavor_id;
	}
	
	public String getFlavor_name() {
		return flavor_name;
	}
	public void setFlavor_name(String flavor_name) {
		this.flavor_name = flavor_name;
	}
	
	public String getKeypair_name() {
		return keypair_name;
	}
	public void setKeypair_name(String keypair_name) {
		this.keypair_name = keypair_name;
	}
	
	public List<ScaleNetworkRefItemVO> getNetwork_info() {
		return network_info;
	}
	public void setNetwork_info(List<ScaleNetworkRefItemVO> network_info) {
		this.network_info = network_info;
	}
	
	public List<String> getInstance_name() {
		return instance_name;
	}
	public void setInstance_name(List<String> instance_name) {
		this.instance_name = instance_name;
	}
	
}
