package com.zodiackillerciphers.old;

import ec.EvolutionState;
import ec.simple.SimpleStatistics;
import ec.util.Output;

import gnu.trove.*;

public class VertexStatistics extends SimpleStatistics {

	public void postEvaluationStatistics(EvolutionState state) {
		super.postEvaluationStatistics(state);
		
		
		THashMap<Float, Integer> stats = new THashMap<Float, Integer>(); 
		float sum = 0;
		float f;
		for (int i=0; i<state.population.subpops[0].individuals.length; i++) {
			f = state.population.subpops[0].individuals[i].fitness.fitness();
			sum += f;
			if (stats.get(f) == null)
				stats.put(f, 0);
			stats.put(f, stats.get(f)+1);
		}
		float mean = sum / state.population.subpops[0].individuals.length;
        state.output.println("Mean: " + mean,Output.V_NO_GENERAL,statisticslog);
        for (Float key : stats.keySet())
            state.output.println("Fit: " + key + ", total: " + stats.get(key),Output.V_NO_GENERAL,statisticslog);
        	
		
        
        
	}

}
