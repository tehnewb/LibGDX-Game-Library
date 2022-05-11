package game.library.font;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.Hinting;

public class FontBuilder {

	private final FreeTypeFontParameter parameter;

	public FontBuilder() {
		this.parameter = new FreeTypeFontParameter();
	}

	public FontBuilder size(int size) {
		this.parameter.size = size;
		return this;
	}

	public FontBuilder shadowOffsetX(int shadowOffsetX) {
		this.parameter.shadowOffsetX = shadowOffsetX;
		return this;
	}

	public FontBuilder shadowOffsetY(int shadowOffsetY) {
		this.parameter.shadowOffsetY = shadowOffsetY;
		return this;
	}

	public FontBuilder mono(boolean mono) {
		this.parameter.mono = mono;
		return this;
	}

	public FontBuilder hinting(Hinting hinting) {
		this.parameter.hinting = hinting;
		return this;
	}

	public FontBuilder color(Color color) {
		this.parameter.color = color;
		return this;
	}

	public FontBuilder borderColor(Color borderColor) {
		this.parameter.borderColor = borderColor;
		return this;
	}

	public FontBuilder borderWidth(float borderWidth) {
		this.parameter.borderWidth = borderWidth;
		return this;
	}

	public FontBuilder padTop(int padTop) {
		this.parameter.padTop = padTop;
		return this;
	}

	public FontBuilder padLeft(int padLeft) {
		this.parameter.padLeft = padLeft;
		return this;
	}

	public FontBuilder padRight(int padRight) {
		this.parameter.padRight = padRight;
		return this;
	}

	public FontBuilder padBottom(int padBottom) {
		this.parameter.padBottom = padBottom;
		return this;
	}

	public FontBuilder borderGamma(float borderGamma) {
		this.parameter.borderGamma = borderGamma;
		return this;
	}

	public FontBuilder shadowColor(Color shadowColor) {
		this.parameter.shadowColor = shadowColor;
		return this;
	}

	public FontBuilder spaceX(int spaceX) {
		this.parameter.spaceX = spaceX;
		return this;
	}

	public FontBuilder spaceY(int spaceY) {
		this.parameter.spaceY = spaceY;
		return this;
	}

	public FontBuilder incremental(boolean incremental) {
		this.parameter.incremental = incremental;
		return this;
	}

	public FontBuilder genMipMaps(boolean genMipMaps) {
		this.parameter.genMipMaps = genMipMaps;
		return this;
	}

	public FontBuilder flip(boolean flip) {
		this.parameter.flip = flip;
		return this;
	}

	public FontBuilder kerning(boolean kerning) {
		this.parameter.kerning = kerning;
		return this;
	}

	public FontBuilder character(String characters) {
		this.parameter.characters = characters;
		return this;
	}

	public FreeTypeFontParameter toParameter() {
		return this.parameter;
	}

	public static FontBuilder create() {
		return new FontBuilder();
	}
}
