/*
 * Copyright (c) 2003-2006 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.java.dev.aircarrier.scene.test;

import net.java.dev.aircarrier.scene.SmokerFactory;

import com.jme.app.SimpleGame;
import com.jme.math.Vector3f;
import com.jmex.effects.particles.ParticleMesh;

public class TestSmokerFactory extends SimpleGame {

  private ParticleMesh pMesh;
  private Vector3f currentPos = new Vector3f(), newPos = new Vector3f();
  private float frameRate = 0;

  public static void main(String[] args) {
    TestSmokerFactory app = new TestSmokerFactory();
    app.setDialogBehaviour(ALWAYS_SHOW_PROPS_DIALOG);
    app.start();
  }

  protected void simpleUpdate() {
    if (tpf > 1f) tpf = 1.0f; // do this to prevent a long pause at start

    if ( (int) currentPos.x == (int) newPos.x
        && (int) currentPos.y == (int) newPos.y
        && (int) currentPos.z == (int) newPos.z) {
      newPos.x = (float) Math.random() * 50 - 25;
      newPos.y = (float) Math.random() * 50 - 25;
      newPos.z = (float) Math.random() * 50 - 150;
    }

    frameRate = timer.getFrameRate() / 2;
    currentPos.x -= (currentPos.x - newPos.x)
        / frameRate;
    currentPos.y -= (currentPos.y - newPos.y)
        / frameRate;
    currentPos.z -= (currentPos.z - newPos.z)
        / frameRate;

    pMesh.setOriginOffset(currentPos);

  }

  protected void simpleInitGame() {
    display.setTitle("Particle System");
    lightState.setEnabled(false);

    pMesh = SmokerFactory.makeSmoker();

    rootNode.attachChild(pMesh);
  }
}
