import { Group } from '@vx/group';
import React from 'react';
import cx from 'classnames';
import PropTypes from 'prop-types';
import { symbol, symbolCross, symbolDiamond, symbolStar, symbolTriangle, symbolWye, symbolSquare, symbolCircle } from 'd3-shape';

Glyph.propTypes = {
  top: PropTypes.number,
  left: PropTypes.number,
  className: PropTypes.string,
  children: PropTypes.any
};
function Glyph(_ref) {
  var _ref$top = _ref.top,
      top = _ref$top === void 0 ? 0 : _ref$top,
      _ref$left = _ref.left,
      left = _ref$left === void 0 ? 0 : _ref$left,
      className = _ref.className,
      children = _ref.children;
  return React.createElement(Group, {
    className: cx('vx-glyph', className),
    top: top,
    left: left
  }, children);
}

function _extends() {
  _extends = Object.assign || function (target) {
    for (var i = 1; i < arguments.length; i++) {
      var source = arguments[i];

      for (var key in source) {
        if (Object.prototype.hasOwnProperty.call(source, key)) {
          target[key] = source[key];
        }
      }
    }

    return target;
  };

  return _extends.apply(this, arguments);
}

function _objectWithoutPropertiesLoose(source, excluded) {
  if (source == null) return {};
  var target = {};
  var sourceKeys = Object.keys(source);
  var key, i;

  for (i = 0; i < sourceKeys.length; i++) {
    key = sourceKeys[i];
    if (excluded.indexOf(key) >= 0) continue;
    target[key] = source[key];
  }

  return target;
}

function _objectWithoutProperties(source, excluded) {
  if (source == null) return {};

  var target = _objectWithoutPropertiesLoose(source, excluded);

  var key, i;

  if (Object.getOwnPropertySymbols) {
    var sourceSymbolKeys = Object.getOwnPropertySymbols(source);

    for (i = 0; i < sourceSymbolKeys.length; i++) {
      key = sourceSymbolKeys[i];
      if (excluded.indexOf(key) >= 0) continue;
      if (!Object.prototype.propertyIsEnumerable.call(source, key)) continue;
      target[key] = source[key];
    }
  }

  return target;
}

GlyphDot.propTypes = {
  children: PropTypes.func,
  className: PropTypes.string,
  top: PropTypes.number,
  left: PropTypes.number
};
function GlyphDot(_ref) {
  var _ref$top = _ref.top,
      top = _ref$top === void 0 ? 0 : _ref$top,
      _ref$left = _ref.left,
      left = _ref$left === void 0 ? 0 : _ref$left,
      className = _ref.className,
      children = _ref.children,
      restProps = _objectWithoutProperties(_ref, ["top", "left", "className", "children"]);

  return React.createElement(Glyph, {
    top: top,
    left: left
  }, React.createElement("circle", _extends({
    className: cx('vx-glyph-dot', className)
  }, restProps)));
}

GlyphCross.propTypes = {
  children: PropTypes.func,
  className: PropTypes.string,
  top: PropTypes.number,
  left: PropTypes.number,
  size: PropTypes.oneOfType([PropTypes.number, PropTypes.func])
};
function GlyphCross(_ref) {
  var children = _ref.children,
      className = _ref.className,
      top = _ref.top,
      left = _ref.left,
      size = _ref.size,
      restProps = _objectWithoutProperties(_ref, ["children", "className", "top", "left", "size"]);

  var path = symbol();
  path.type(symbolCross);
  if (size) path.size(size);
  if (children) return children({
    path: path
  });
  return React.createElement(Glyph, {
    top: top,
    left: left
  }, React.createElement("path", _extends({
    className: cx('vx-glyph-cross', className),
    d: path()
  }, restProps)));
}

GlyphDiamond.propTypes = {
  children: PropTypes.func,
  className: PropTypes.string,
  top: PropTypes.number,
  left: PropTypes.number,
  size: PropTypes.oneOfType([PropTypes.number, PropTypes.func])
};
function GlyphDiamond(_ref) {
  var children = _ref.children,
      className = _ref.className,
      top = _ref.top,
      left = _ref.left,
      size = _ref.size,
      restProps = _objectWithoutProperties(_ref, ["children", "className", "top", "left", "size"]);

  var path = symbol();
  path.type(symbolDiamond);
  if (size) path.size(size);
  if (children) return children({
    path: path
  });
  return React.createElement(Glyph, {
    top: top,
    left: left
  }, React.createElement("path", _extends({
    className: cx('vx-glyph-diamond', className),
    d: path()
  }, restProps)));
}

GlyphStar.propTypes = {
  children: PropTypes.func,
  className: PropTypes.string,
  top: PropTypes.number,
  left: PropTypes.number,
  size: PropTypes.oneOfType([PropTypes.number, PropTypes.func])
};
function GlyphStar(_ref) {
  var children = _ref.children,
      className = _ref.className,
      top = _ref.top,
      left = _ref.left,
      size = _ref.size,
      restProps = _objectWithoutProperties(_ref, ["children", "className", "top", "left", "size"]);

  var path = symbol();
  path.type(symbolStar);
  if (size) path.size(size);
  if (children) return children({
    path: path
  });
  return React.createElement(Glyph, {
    top: top,
    left: left
  }, React.createElement("path", _extends({
    className: cx('vx-glyph-star', className),
    d: path()
  }, restProps)));
}

GlyphTriangle.propTypes = {
  children: PropTypes.func,
  className: PropTypes.string,
  top: PropTypes.number,
  left: PropTypes.number,
  size: PropTypes.oneOfType([PropTypes.number, PropTypes.func])
};
function GlyphTriangle(_ref) {
  var children = _ref.children,
      className = _ref.className,
      top = _ref.top,
      left = _ref.left,
      size = _ref.size,
      restProps = _objectWithoutProperties(_ref, ["children", "className", "top", "left", "size"]);

  var path = symbol();
  path.type(symbolTriangle);
  if (size) path.size(size);
  if (children) return children({
    path: path
  });
  return React.createElement(Glyph, {
    top: top,
    left: left
  }, React.createElement("path", _extends({
    className: cx('vx-glyph-triangle', className),
    d: path()
  }, restProps)));
}

GlyphWye.propTypes = {
  children: PropTypes.func,
  className: PropTypes.string,
  top: PropTypes.number,
  left: PropTypes.number,
  size: PropTypes.oneOfType([PropTypes.number, PropTypes.func])
};
function GlyphWye(_ref) {
  var children = _ref.children,
      className = _ref.className,
      top = _ref.top,
      left = _ref.left,
      size = _ref.size,
      restProps = _objectWithoutProperties(_ref, ["children", "className", "top", "left", "size"]);

  var path = symbol();
  path.type(symbolWye);
  if (size) path.size(size);
  if (children) return children({
    path: path
  });
  return React.createElement(Glyph, {
    top: top,
    left: left
  }, React.createElement("path", _extends({
    className: cx('vx-glyph-wye', className),
    d: path()
  }, restProps)));
}

GlyphSquare.propTypes = {
  children: PropTypes.func,
  className: PropTypes.string,
  top: PropTypes.number,
  left: PropTypes.number,
  size: PropTypes.oneOfType([PropTypes.number, PropTypes.func])
};
function GlyphSquare(_ref) {
  var children = _ref.children,
      className = _ref.className,
      top = _ref.top,
      left = _ref.left,
      size = _ref.size,
      restProps = _objectWithoutProperties(_ref, ["children", "className", "top", "left", "size"]);

  var path = symbol();
  path.type(symbolSquare);
  if (size) path.size(size);
  if (children) return children({
    path: path
  });
  return React.createElement(Glyph, {
    top: top,
    left: left
  }, React.createElement("path", _extends({
    className: cx('vx-glyph-square', className),
    d: path()
  }, restProps)));
}

GlyphCircle.propTypes = {
  children: PropTypes.func,
  className: PropTypes.string,
  top: PropTypes.number,
  left: PropTypes.number,
  size: PropTypes.oneOfType([PropTypes.number, PropTypes.func])
};
function GlyphCircle(_ref) {
  var children = _ref.children,
      className = _ref.className,
      top = _ref.top,
      left = _ref.left,
      size = _ref.size,
      restProps = _objectWithoutProperties(_ref, ["children", "className", "top", "left", "size"]);

  var path = symbol();
  path.type(symbolCircle);
  if (size) path.size(size);
  if (children) return children({
    path: path
  });
  return React.createElement(Glyph, {
    top: top,
    left: left
  }, React.createElement("path", _extends({
    className: cx('vx-glyph-circle', className),
    d: path()
  }, restProps)));
}

export { Glyph, GlyphDot, GlyphCross, GlyphDiamond, GlyphStar, GlyphTriangle, GlyphWye, GlyphSquare, GlyphCircle };
