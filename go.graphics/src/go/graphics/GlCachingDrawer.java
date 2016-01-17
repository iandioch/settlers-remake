/*******************************************************************************
 * Copyright (c) 2015
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package go.graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public abstract class GlCachingDrawer implements GLDrawContext {
	private static final int GL_BUFFERS = 4;
	private static final int GL_BUFFER_TRIANGLES = 100;

	private static final byte[] WHITE = new byte[] {
			(byte) 255, (byte) 255, (byte) 255, (byte) 255
	};

	private static final byte[] activeColor = WHITE;

	private class GLBuffer {
		/**
		 * Bytes we need for one vertex
		 */
		private static final int VERTEX_LENGTH = 5 * 4 + 4;
		private static final int TRIAMGLE_LENGTH = 3 * VERTEX_LENGTH;

		protected final ByteBuffer byteBuffer;
		protected final FloatBuffer reuseableBuffer;
		protected final FloatBuffer reuseableBufferDuplicate;

		protected GLBuffer() {
			byteBuffer =
					ByteBuffer.allocateDirect(GL_BUFFER_TRIANGLES * TRIAMGLE_LENGTH);
			byteBuffer.order(ByteOrder.nativeOrder());
			reuseableBuffer = byteBuffer.asFloatBuffer();
			reuseableBufferDuplicate = reuseableBuffer.duplicate();
		}

		/**
		 * The last texture we set.
		 */
		private TextureHandle currentTexture = null;

		private int currentTriangles = 0;

		protected void addTriangles(float[] triangles) {
			for (int i = 0; i < triangles.length; i += 5 * 3) {
				addTexturedTriangle(triangles, i);
			}
		}

		private void addTexturedTriangle(float[] triangles, int offset) {
			for (int i = 0; i < 3; i++) {
				addTexturedPoint(triangles, offset + i * 5);
			}
			currentTriangles++;
		}

		private void addTexturedPoint(float[] triangles, int offset) {
			for (int i = 0; i < 5; i++) {
				byteBuffer.putFloat(triangles[offset + i]);
			}
			byteBuffer.put(activeColor);
		}

		protected void setForTexture(TextureHandle texture) {
			if (currentTexture != texture) {
				if (currentTriangles != 0) {
					draw();
				}
				currentTexture = texture;
			}
		}

		private void draw() {
			setTexture(currentTexture);
			reuseableBuffer.rewind();
			reuseableBufferDuplicate.rewind();
			drawTriangles(reuseableBuffer, reuseableBufferDuplicate,
					currentTriangles);
			byteBuffer.rewind();
			currentTriangles = 0;
		}

		public void addQuad(float[] geometry) {
			addTexturedPoint(geometry, 0);
			addTexturedPoint(geometry, 5);
			addTexturedPoint(geometry, 10);
			addTexturedPoint(geometry, 5);
			addTexturedPoint(geometry, 10);
			addTexturedPoint(geometry, 15);
			currentTriangles += 2;
		}

		public void addQuadPrimitive(float x1, float y1, float x2, float y2) {
			addPointPrimitive(x1, y1);
			addPointPrimitive(x1, y2);
			addPointPrimitive(x2, y1);
			addPointPrimitive(x2, y1);
			addPointPrimitive(x1, y2);
			addPointPrimitive(x2, y2);
			currentTriangles += 2;
		}

		private void addPointPrimitive(float x1, float y1) {
			byteBuffer.putFloat(x1);
			byteBuffer.putFloat(y1);
			byteBuffer.putFloat(0);
			byteBuffer.putFloat(0);
			byteBuffer.putFloat(0);
			byteBuffer.put(activeColor);
		}

	}

	private int lastFreedBuffer = 0;
	private final GLBuffer[] drawBuffers;

	protected GlCachingDrawer() {
		drawBuffers = new GLBuffer[GL_BUFFERS];
		for (int i = 0; i < GL_BUFFERS; i++) {
			drawBuffers[i] = new GLBuffer();
		}
	}

	public void flush() {
		for (int i = 0; i < GL_BUFFERS; i++) {
			drawBuffers[i].draw();
		}
	}

	protected abstract void drawTriangles(FloatBuffer buffer,
			FloatBuffer bufferDuplicate, int tris);

	protected abstract void setTexture(TextureHandle index);

	protected GLBuffer getBuffer(TextureHandle texture) {
		for (int i = 0; i < GL_BUFFERS; i++) {
			if (drawBuffers[i].currentTexture == texture) {
				return drawBuffers[i];
			}
		}

		lastFreedBuffer++;
		if (lastFreedBuffer >= GL_BUFFERS) {
			lastFreedBuffer = 0;
		}

		GLBuffer buffer = drawBuffers[lastFreedBuffer];
		buffer.setForTexture(texture);
		return buffer;
	}

	@Override
	public void drawTrianglesWithTexture(TextureHandle textureid, float[] geometry) {
		GLBuffer buffer = getBuffer(textureid);
		buffer.addTriangles(geometry);
	}

	@Override
	public void color(float red, float green, float blue, float alpha) {
		activeColor[0] = (byte) (red * 255);
		activeColor[1] = (byte) (green * 255);
		activeColor[2] = (byte) (blue * 255);
		activeColor[3] = (byte) (alpha * 255);
	}

	@Override
	public void drawQuadWithTexture(TextureHandle textureid, float[] geometry) {
		GLBuffer buffer = getBuffer(textureid);
		buffer.addQuad(geometry);
	}

	@Override
	public void fillQuad(float x1, float y1, float x2, float y2) {
		GLBuffer buffer = getBuffer(null);
		buffer.addQuadPrimitive(x1, y1, x2, y2);
	}

	@Override
	public void drawTrianglesWithTextureColored(TextureHandle textureid, float[] geometry) {
		// UNUSED
	}

}