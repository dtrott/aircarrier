package net.java.dev.aircarrier.util;

import com.jme.scene.Spatial;
import com.jme.scene.state.RenderState;

/**
 * Clear all render states (check list, there might be new ones that aren't covered!) on spatials.
 * @author goki
 */
public class SpatialRenderStateClearer implements SpatialAction {

	private static SpatialRenderStateClearer instance = new SpatialRenderStateClearer();

	/**
	 * @return
	 * 		A single shared instance of the clearer, since it has no state
	 */
	public static SpatialRenderStateClearer getInstance() {
		return instance;
	}

	private SpatialRenderStateClearer() {
	}

	public void actOnSpatial(Spatial spatial) {
        spatial.clearRenderState(RenderState.RS_BLEND);
		spatial.clearRenderState(RenderState.RS_CLIP);
		spatial.clearRenderState(RenderState.RS_COLORMASK_STATE);
		spatial.clearRenderState(RenderState.RS_CULL);
		spatial.clearRenderState(RenderState.RS_FOG);
		spatial.clearRenderState(RenderState.RS_FRAGMENT_PROGRAM);
		spatial.clearRenderState(RenderState.RS_GLSL_SHADER_OBJECTS);
		spatial.clearRenderState(RenderState.RS_LIGHT);
		spatial.clearRenderState(RenderState.RS_MATERIAL);
		spatial.clearRenderState(RenderState.RS_SHADE);
		spatial.clearRenderState(RenderState.RS_STENCIL);
		spatial.clearRenderState(RenderState.RS_TEXTURE);
		spatial.clearRenderState(RenderState.RS_VERTEX_PROGRAM);
		spatial.clearRenderState(RenderState.RS_WIREFRAME);
		spatial.clearRenderState(RenderState.RS_ZBUFFER);
	}

	public void actOnSpatial(Spatial spatial, int level) {
		actOnSpatial(spatial);
	}

}
