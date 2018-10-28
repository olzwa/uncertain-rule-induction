package induction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * [{
 * "premises" : [ {
 * "head" : "płeć ",
 * "set" : [ "kobieta" ],
 * "conjunction" : true
 * } ],
 * "conclusion" : {
 * "head" : "wiek",
 * "set" : [ "23" ],
 * "conjunction" : true
 * },
 * "GrfIrf" : {
 * "irf" : 0.5,
 * "grf" : 1
 * },
 * "debugInfo" : {
 * "conclusionsMatchedCounter" : 2,
 * "premiseMatchedCounter" : 4,
 * "totalRows" : 4
 * }
 * }
 **/
public class SimpleRule {

	private Set<JsonNode> premises;

	private JsonNode conclusion;

	@JsonProperty("GrfIrf")
	private GrfIrf GrfIrf;

	private JsonNode debugInfo;

	public SimpleRule(Set<JsonNode> premises, JsonNode conclusion, GrfIrf grfIrf, JsonNode debugInfo) {
		this.premises = premises;
		this.conclusion = conclusion;
		GrfIrf = grfIrf;
		this.debugInfo = debugInfo;
	}

	public SimpleRule() {
	}

	public Set<JsonNode> getPremises() {
		return premises;
	}

	public JsonNode getConclusion() {
		return conclusion;
	}

	public GrfIrf getGrfIrf() {
		return GrfIrf;
	}

	public JsonNode getDebugInfo() {
		return debugInfo;
	}

	public void setPremises(Set<JsonNode> premises) {
		this.premises = premises;
	}

	public void setConclusion(JsonNode conclusion) {
		this.conclusion = conclusion;
	}

	public void setGrfIrf(GrfIrf grfIrf) {
		GrfIrf = grfIrf;
	}

	public void setDebugInfo(JsonNode debugInfo) {
		this.debugInfo = debugInfo;
	}

	@Override
	public String toString() {
		return "SimpleRule{" +
				"premises=" + premises +
				", conclusion=" + conclusion +
				", GrfIrf=" + GrfIrf +
				", debugInfo=" + debugInfo +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SimpleRule that = (SimpleRule) o;
		return Objects.equals(premises, that.premises) &&
				Objects.equals(conclusion, that.conclusion) &&
				Objects.equals(GrfIrf, that.GrfIrf) &&
				Objects.equals(debugInfo, that.debugInfo);
	}

	@Override
	public int hashCode() {
		return Objects.hash(premises, conclusion, GrfIrf, debugInfo);
	}
}

	class GrfIrf {
	private String grf;
	private String irf;

	public GrfIrf() {
	}

	public String getGrf() {
		return grf;
	}

	public String getIrf() {
		return irf;
	}

	public void setGrf(String grf) {
		this.grf = grf;
	}

	public void setIrf(String irf) {
		this.irf = irf;
	}

	@Override
	public String toString() {
		return "GrfIrf{" +
				"grf='" + grf + '\'' +
				", irf='" + irf + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		GrfIrf grfIrf = (GrfIrf) o;
		return Objects.equals(grf, grfIrf.grf) &&
				Objects.equals(irf, grfIrf.irf);
	}

	@Override
	public int hashCode() {
		return Objects.hash(grf, irf);
	}
}
