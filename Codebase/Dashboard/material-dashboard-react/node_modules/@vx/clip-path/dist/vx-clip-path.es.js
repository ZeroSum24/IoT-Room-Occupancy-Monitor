import React from 'react';
import PropTypes from 'prop-types';

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

ClipPath.propTypes = {
  id: PropTypes.string.isRequired,
  children: PropTypes.any
};
function ClipPath(_ref) {
  var id = _ref.id,
      children = _ref.children,
      restProps = _objectWithoutProperties(_ref, ["id", "children"]);

  return React.createElement("defs", null, React.createElement("clipPath", _extends({
    id: id
  }, restProps), children));
}

CircleClipPath.propTypes = {
  id: PropTypes.string.isRequired,
  cx: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  cy: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  r: PropTypes.oneOfType([PropTypes.string, PropTypes.number])
};
function CircleClipPath(_ref) {
  var id = _ref.id,
      cx = _ref.cx,
      cy = _ref.cy,
      r = _ref.r,
      restProps = _objectWithoutProperties(_ref, ["id", "cx", "cy", "r"]);

  return React.createElement(ClipPath, {
    id: id
  }, React.createElement("circle", _extends({
    cx: cx,
    cy: cy,
    r: r
  }, restProps)));
}

RectClipPath.propTypes = {
  id: PropTypes.string.isRequired,
  x: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  y: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  width: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  height: PropTypes.oneOfType([PropTypes.string, PropTypes.number])
};
function RectClipPath(_ref) {
  var id = _ref.id,
      _ref$x = _ref.x,
      x = _ref$x === void 0 ? 0 : _ref$x,
      _ref$y = _ref.y,
      y = _ref$y === void 0 ? 0 : _ref$y,
      _ref$width = _ref.width,
      width = _ref$width === void 0 ? 1 : _ref$width,
      _ref$height = _ref.height,
      height = _ref$height === void 0 ? 1 : _ref$height,
      restProps = _objectWithoutProperties(_ref, ["id", "x", "y", "width", "height"]);

  return React.createElement(ClipPath, {
    id: id
  }, React.createElement("rect", _extends({
    x: x,
    y: y,
    width: width,
    height: height
  }, restProps)));
}

export { ClipPath, CircleClipPath, RectClipPath };
