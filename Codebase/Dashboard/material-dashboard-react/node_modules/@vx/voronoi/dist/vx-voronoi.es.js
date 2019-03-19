import { voronoi } from 'd3-voronoi';
import React from 'react';
import cx from 'classnames';
import PropTypes from 'prop-types';

// returns a d3 voronoi *layout*, for a voronoi *diagram* call `layout(data)`
var voronoi$1 = (function (_ref) {
  var _ref$width = _ref.width,
      width = _ref$width === void 0 ? 0 : _ref$width,
      _ref$height = _ref.height,
      height = _ref$height === void 0 ? 0 : _ref$height,
      x = _ref.x,
      y = _ref.y;
  var voronoi$$1 = voronoi();
  if (x) voronoi$$1.x(x);
  if (y) voronoi$$1.y(y);
  voronoi$$1.extent([[-1, -1], [width + 1, height + 1]]);
  return voronoi$$1;
});

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

VoronoiPolygon.propTypes = {
  polygon: PropTypes.arrayOf(PropTypes.array),
  className: PropTypes.string,
  children: PropTypes.func
};
function VoronoiPolygon(_ref) {
  var polygon = _ref.polygon,
      className = _ref.className,
      children = _ref.children,
      restProps = _objectWithoutProperties(_ref, ["polygon", "className", "children"]);

  if (!polygon) return null;
  var path = "M".concat(polygon.join('L'), "Z");
  if (children) return children({
    path: path,
    polygon: polygon
  });
  return React.createElement("path", _extends({
    className: cx('vx-voronoi-polygon', className),
    d: path
  }, restProps));
}

export { voronoi$1 as voronoi, VoronoiPolygon };
