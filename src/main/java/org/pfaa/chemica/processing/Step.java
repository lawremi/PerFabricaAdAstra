package org.pfaa.chemica.processing;

import java.util.List;

public interface Step {
	public double getHeatChange();
	public List<Stream> getInputs();
	public List<Stream> getOutputs();
}
