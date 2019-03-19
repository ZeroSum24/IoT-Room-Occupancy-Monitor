import React from 'react';
import PropTypes from 'prop-types';
import cx from 'classnames';

function Pattern(_ref) {
  var id = _ref.id,
      width = _ref.width,
      height = _ref.height,
      children = _ref.children;
  return React.createElement("defs", null, React.createElement("pattern", {
    id: id,
    width: width,
    height: height,
    patternUnits: "userSpaceOnUse"
  }, children));
}
Pattern.propTypes = {
  id: PropTypes.string.isRequired,
  width: PropTypes.number.isRequired,
  height: PropTypes.number.isRequired,
  children: PropTypes.oneOfType([PropTypes.arrayOf(PropTypes.node), PropTypes.node]).isRequired
};

var Orientation = {
  horizontal: 'horizontal',
  vertical: 'vertical',
  diagonal: 'diagonal'
};

function pathForOrientation(_ref) {
  var height = _ref.height,
      orientation = _ref.orientation;
  var path;

  switch (orientation) {
    case Orientation.vertical:
      path = "M ".concat(height / 2, ", 0 l 0, ").concat(height);
      break;

    case Orientation.horizontal:
      path = "M 0,".concat(height / 2, " l ").concat(height, ",0");
      break;

    case Orientation.diagonal:
      path = "M 0,".concat(height, " l ").concat(height, ",").concat(-height, " M ").concat(-height / 4, ",").concat(height / 4, " l ").concat(height / 2, ",").concat(-height / 2, "\n             M ").concat(3 / 4 * height, ",").concat(5 / 4 * height, " l ").concat(height / 2, ",").concat(-height / 2);
      break;

    default:
      path = "M ".concat(height / 2, ", 0 l 0, ").concat(height);
  }

  return path;
}

function PatternLines(_ref2) {
  var id = _ref2.id,
      width = _ref2.width,
      height = _ref2.height,
      stroke = _ref2.stroke,
      strokeWidth = _ref2.strokeWidth,
      strokeDasharray = _ref2.strokeDasharray,
      _ref2$strokeLinecap = _ref2.strokeLinecap,
      strokeLinecap = _ref2$strokeLinecap === void 0 ? 'square' : _ref2$strokeLinecap,
      _ref2$shapeRendering = _ref2.shapeRendering,
      shapeRendering = _ref2$shapeRendering === void 0 ? 'auto' : _ref2$shapeRendering,
      _ref2$orientation = _ref2.orientation,
      orientation = _ref2$orientation === void 0 ? ['vertical'] : _ref2$orientation,
      background = _ref2.background,
      className = _ref2.className;
  if (!Array.isArray(orientation)) orientation = [orientation];
  return React.createElement(Pattern, {
    id: id,
    width: width,
    height: height
  }, !!background && React.createElement("rect", {
    className: cx('vx-pattern-line-background'),
    width: width,
    height: height,
    fill: background
  }), orientation.map(function (o, i) {
    return React.createElement("path", {
      key: "vx-".concat(id, "-line-").concat(o, "-").concat(i),
      className: cx('vx-pattern-line', className),
      d: pathForOrientation({
        orientation: o,
        height: height
      }),
      stroke: stroke,
      strokeWidth: strokeWidth,
      strokeDasharray: strokeDasharray,
      strokeLinecap: strokeLinecap,
      shapeRendering: shapeRendering
    });
  }));
}
PatternLines.propTypes = {
  id: PropTypes.string.isRequired,
  width: PropTypes.number.isRequired,
  height: PropTypes.number.isRequired,
  background: PropTypes.string,
  stroke: PropTypes.string.isRequired,
  strokeWidth: PropTypes.number.isRequired,
  strokeDasharray: PropTypes.string,
  className: PropTypes.string,
  strokeLinecap: PropTypes.string,
  shapeRendering: PropTypes.string,
  orientation: PropTypes.array
};

function _slicedToArray(arr, i) {
  return _arrayWithHoles(arr) || _iterableToArrayLimit(arr, i) || _nonIterableRest();
}

function _arrayWithHoles(arr) {
  if (Array.isArray(arr)) return arr;
}

function _iterableToArrayLimit(arr, i) {
  var _arr = [];
  var _n = true;
  var _d = false;
  var _e = undefined;

  try {
    for (var _i = arr[Symbol.iterator](), _s; !(_n = (_s = _i.next()).done); _n = true) {
      _arr.push(_s.value);

      if (i && _arr.length === i) break;
    }
  } catch (err) {
    _d = true;
    _e = err;
  } finally {
    try {
      if (!_n && _i["return"] != null) _i["return"]();
    } finally {
      if (_d) throw _e;
    }
  }

  return _arr;
}

function _nonIterableRest() {
  throw new TypeError("Invalid attempt to destructure non-iterable instance");
}

/**
 * Creates an array of cirlces for a list of corners
 * in the format [[cornerX, cornerY], ...]
 */

function createCircles(_ref) {
  var corners = _ref.corners,
      id = _ref.id,
      radius = _ref.radius,
      fill = _ref.fill,
      stroke = _ref.stroke,
      strokeWidth = _ref.strokeWidth,
      strokeDasharray = _ref.strokeDasharray,
      className = _ref.className;
  return corners.map(function (_ref2) {
    var _ref3 = _slicedToArray(_ref2, 2),
        cornerX = _ref3[0],
        cornerY = _ref3[1];

    return React.createElement("circle", {
      key: "".concat(id, "-complement-").concat(cornerX, "-").concat(cornerY),
      className: cx('vx-pattern-circle vx-pattern-circle-complement', className),
      cx: cornerX,
      cy: cornerY,
      r: radius,
      fill: fill,
      stroke: stroke,
      strokeWidth: strokeWidth,
      strokeDasharray: strokeDasharray
    });
  });
}
PatternCircles.propTypes = {
  id: PropTypes.string.isRequired,
  width: PropTypes.number.isRequired,
  height: PropTypes.number.isRequired,
  radius: PropTypes.number,
  fill: PropTypes.string,
  className: PropTypes.string,
  stroke: PropTypes.string,
  strokeWidth: PropTypes.number,
  strokeDasharray: PropTypes.string,
  complement: PropTypes.bool,
  background: PropTypes.string
};
function PatternCircles(_ref4) {
  var id = _ref4.id,
      width = _ref4.width,
      height = _ref4.height,
      _ref4$radius = _ref4.radius,
      radius = _ref4$radius === void 0 ? 2 : _ref4$radius,
      fill = _ref4.fill,
      stroke = _ref4.stroke,
      strokeWidth = _ref4.strokeWidth,
      strokeDasharray = _ref4.strokeDasharray,
      background = _ref4.background,
      _ref4$complement = _ref4.complement,
      complement = _ref4$complement === void 0 ? false : _ref4$complement,
      className = _ref4.className;
  var corners;

  if (complement) {
    corners = [[0, 0], [0, height], [width, 0], [width, height]];
  }

  return React.createElement(Pattern, {
    id: id,
    width: width,
    height: height
  }, !!background && React.createElement("rect", {
    width: width,
    height: height,
    fill: background
  }), React.createElement("circle", {
    className: cx('vx-pattern-circle', className),
    cx: width / 2,
    cy: height / 2,
    r: radius,
    fill: fill,
    stroke: stroke,
    strokeWidth: strokeWidth,
    strokeDasharray: strokeDasharray
  }), complement && createCircles({
    corners: corners,
    id: id,
    radius: radius,
    fill: fill,
    stroke: stroke,
    strokeWidth: strokeWidth,
    strokeDasharray: strokeDasharray
  }));
}

function PatternPath(_ref) {
  var id = _ref.id,
      width = _ref.width,
      height = _ref.height,
      path = _ref.path,
      _ref$fill = _ref.fill,
      fill = _ref$fill === void 0 ? 'transparent' : _ref$fill,
      stroke = _ref.stroke,
      strokeWidth = _ref.strokeWidth,
      strokeDasharray = _ref.strokeDasharray,
      _ref$strokeLinecap = _ref.strokeLinecap,
      strokeLinecap = _ref$strokeLinecap === void 0 ? 'square' : _ref$strokeLinecap,
      _ref$shapeRendering = _ref.shapeRendering,
      shapeRendering = _ref$shapeRendering === void 0 ? 'auto' : _ref$shapeRendering,
      background = _ref.background,
      className = _ref.className;
  return React.createElement(Pattern, {
    id: id,
    width: width,
    height: height
  }, !!background && React.createElement("rect", {
    width: width,
    height: height,
    fill: background
  }), React.createElement("path", {
    className: cx('vx-pattern-path', className),
    d: path,
    fill: fill,
    stroke: stroke,
    strokeWidth: strokeWidth,
    strokeDasharray: strokeDasharray,
    strokeLinecap: strokeLinecap,
    shapeRendering: shapeRendering
  }));
}
PatternPath.propTypes = {
  id: PropTypes.string.isRequired,
  width: PropTypes.number.isRequired,
  height: PropTypes.number.isRequired,
  path: PropTypes.string,
  fill: PropTypes.string,
  className: PropTypes.string,
  background: PropTypes.string,
  stroke: PropTypes.string,
  strokeWidth: PropTypes.number,
  strokeDasharray: PropTypes.string,
  strokeLinecap: PropTypes.string,
  shapeRendering: PropTypes.string
};

function PatternWaves(_ref) {
  var id = _ref.id,
      width = _ref.width,
      height = _ref.height,
      fill = _ref.fill,
      stroke = _ref.stroke,
      strokeWidth = _ref.strokeWidth,
      strokeDasharray = _ref.strokeDasharray,
      strokeLinecap = _ref.strokeLinecap,
      shapeRendering = _ref.shapeRendering,
      background = _ref.background,
      className = _ref.className;
  return React.createElement(PatternPath, {
    className: cx('vx-pattern-wave', className),
    path: "M 0 ".concat(height / 2, " c ").concat(height / 8, " ").concat(-height / 4, " , ").concat(height * 3 / 8, " ").concat(-height / 4, " , ").concat(height / 2, " 0\n             c ").concat(height / 8, " ").concat(height / 4, " , ").concat(height * 3 / 8, " ").concat(height / 4, " , ").concat(height / 2, " 0 M ").concat(-height / 2, " ").concat(height / 2, "\n             c ").concat(height / 8, " ").concat(height / 4, " , ").concat(height * 3 / 8, " ").concat(height / 4, " , ").concat(height / 2, " 0 M ").concat(height, " ").concat(height / 2, "\n             c ").concat(height / 8, " ").concat(-height / 4, " , ").concat(height * 3 / 8, " ").concat(-height / 4, " , ").concat(height / 2, " 0"),
    id: id,
    width: width,
    height: height,
    fill: fill,
    stroke: stroke,
    strokeWidth: strokeWidth,
    strokeDasharray: strokeDasharray,
    strokeLinecap: strokeLinecap,
    shapeRendering: shapeRendering,
    background: background
  });
}
PatternWaves.propTypes = {
  id: PropTypes.string.isRequired,
  width: PropTypes.number.isRequired,
  height: PropTypes.number.isRequired,
  fill: PropTypes.string,
  className: PropTypes.string,
  background: PropTypes.string,
  stroke: PropTypes.string,
  strokeWidth: PropTypes.number,
  strokeDasharray: PropTypes.string,
  strokeLinecap: PropTypes.string,
  shapeRendering: PropTypes.string
};

PatternHexagons.propTypes = {
  id: PropTypes.string.isRequired,
  width: PropTypes.number.isRequired,
  height: PropTypes.number.isRequired,
  size: PropTypes.number,
  fill: PropTypes.string,
  className: PropTypes.string,
  background: PropTypes.string,
  stroke: PropTypes.string,
  strokeWidth: PropTypes.number,
  strokeDasharray: PropTypes.string,
  strokeLinecap: PropTypes.string,
  shapeRendering: PropTypes.string
};
function PatternHexagons(_ref) {
  var id = _ref.id,
      width = _ref.width,
      height = _ref.height,
      fill = _ref.fill,
      stroke = _ref.stroke,
      strokeWidth = _ref.strokeWidth,
      strokeDasharray = _ref.strokeDasharray,
      strokeLinecap = _ref.strokeLinecap,
      shapeRendering = _ref.shapeRendering,
      background = _ref.background,
      className = _ref.className,
      _ref$size = _ref.size,
      size = _ref$size === void 0 ? 3 : _ref$size;
  var s = Math.sqrt(size);
  return React.createElement(PatternPath, {
    className: cx('vx-pattern-hexagon', className),
    path: "M ".concat(height, ",0 l ").concat(height, ",0 l ").concat(height / 2, ",").concat(height * s / 2, " l ").concat(-height / 2, ",").concat(height * s / 2, " l ").concat(-height, ",0 l ").concat(-height / 2, ",").concat(-height * s / 2, " Z M 0,").concat(height * s / 2, " l ").concat(height / 2, ",0 M ").concat(3 * height, ",").concat(height * s / 2, " l ").concat(-height / 2, ",0"),
    id: id,
    width: size,
    height: s,
    fill: fill,
    stroke: stroke,
    strokeWidth: strokeWidth,
    strokeDasharray: strokeDasharray,
    strokeLinecap: strokeLinecap,
    shapeRendering: shapeRendering,
    background: background
  });
}

export { Pattern, PatternLines, PatternCircles, PatternWaves, PatternHexagons, PatternPath, Orientation as PatternOrientation };
