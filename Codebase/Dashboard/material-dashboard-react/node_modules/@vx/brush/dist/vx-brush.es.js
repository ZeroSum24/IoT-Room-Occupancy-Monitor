import { compose, withState, withHandlers } from 'recompose';
import React from 'react';
import cx from 'classnames';

function _defineProperty(obj, key, value) {
  if (key in obj) {
    Object.defineProperty(obj, key, {
      value: value,
      enumerable: true,
      configurable: true,
      writable: true
    });
  } else {
    obj[key] = value;
  }

  return obj;
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

function _objectSpread(target) {
  for (var i = 1; i < arguments.length; i++) {
    var source = arguments[i] != null ? arguments[i] : {};
    var ownKeys = Object.keys(source);

    if (typeof Object.getOwnPropertySymbols === 'function') {
      ownKeys = ownKeys.concat(Object.getOwnPropertySymbols(source).filter(function (sym) {
        return Object.getOwnPropertyDescriptor(source, sym).enumerable;
      }));
    }

    ownKeys.forEach(function (key) {
      _defineProperty(target, key, source[key]);
    });
  }

  return target;
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

var withBrush = compose(withState('brush', 'updateBrush', {
  start: undefined,
  end: undefined,
  domain: undefined,
  isBrushing: false
}), withHandlers({
  onBrushStart: function onBrushStart(_ref) {
    var updateBrush = _ref.updateBrush;
    return function (_ref2) {
      var x = _ref2.x,
          y = _ref2.y;
      updateBrush(function (prevState) {
        return _objectSpread({}, prevState, {
          start: {
            x: x,
            y: y
          },
          isBrushing: true,
          end: undefined,
          domain: undefined
        });
      });
    };
  },
  onBrushDrag: function onBrushDrag(_ref3) {
    var updateBrush = _ref3.updateBrush;
    return function (_ref4) {
      var x = _ref4.x,
          y = _ref4.y;
      updateBrush(function (prevState) {
        return _objectSpread({}, prevState, {
          end: {
            x: x,
            y: y
          },
          domain: undefined
        });
      });
    };
  },
  onBrushEnd: function onBrushEnd(_ref5) {
    var updateBrush = _ref5.updateBrush;
    return function (_ref6) {
      var x = _ref6.x,
          y = _ref6.y;
      updateBrush(function (prevState) {
        var start = prevState.start;
        return _objectSpread({}, prevState, {
          isBrushing: false,
          domain: {
            x0: Math.min(start.x, x),
            x1: Math.max(start.x, x),
            y0: Math.min(start.y, y),
            y1: Math.max(start.y, y)
          }
        });
      });
    };
  },
  onBrushReset: function onBrushReset(_ref7) {
    var updateBrush = _ref7.updateBrush;
    return function (event) {
      updateBrush(function (prevState) {
        return {
          start: undefined,
          end: undefined,
          domain: undefined,
          isBrushing: false
        };
      });
    };
  }
}));

var BoxBrush = (function (props) {
  var brush = props.brush,
      className = props.className,
      _props$fill = props.fill,
      fill = _props$fill === void 0 ? 'rgba(102, 181, 245, 0.1)' : _props$fill,
      _props$stroke = props.stroke,
      stroke = _props$stroke === void 0 ? 'rgba(102, 181, 245, 1)' : _props$stroke,
      _props$strokeWidth = props.strokeWidth,
      strokeWidth = _props$strokeWidth === void 0 ? 1 : _props$strokeWidth,
      otherProps = _objectWithoutProperties(props, ["brush", "className", "fill", "stroke", "strokeWidth"]);

  var start = brush.start,
      end = brush.end,
      isBrushing = brush.isBrushing;
  if (!start) return null;
  if (!end) return null;
  var x = end.x > start.x ? start.x : end.x;
  var y = end.y > start.y ? start.y : end.y;
  var width = Math.abs(start.x - end.x);
  var height = Math.abs(start.y - end.y);
  return React.createElement("g", {
    className: cx('vx-brush', className)
  }, isBrushing && React.createElement("rect", _extends({
    fill: fill,
    stroke: stroke,
    strokeWidth: strokeWidth,
    x: x,
    y: y,
    width: width,
    height: height
  }, otherProps)));
});

function constrainToRegion(_ref) {
  var region = _ref.region,
      x = _ref.x,
      y = _ref.y;
  var x0 = region.x0,
      x1 = region.x1,
      y0 = region.y0,
      y1 = region.y1;
  return {
    x: x < x0 ? x0 : x > x1 ? x1 : x,
    y: y < y0 ? y0 : y > y1 ? y1 : y
  };
}

function getCoordsFromEvent(node, event) {
  if (!node) return;
  var svg = node.ownerSVGElement || node;

  if (svg.createSVGPoint) {
    var point = svg.createSVGPoint();
    point.x = event.clientX;
    point.y = event.clientY;
    point = point.matrixTransform(node.getScreenCTM().inverse());
    return {
      x: point.x,
      y: point.y
    };
  }

  var rect = node.getBoundingClientRect();
  return {
    x: event.clientX - rect.left - node.clientLeft,
    y: event.clientY - rect.top - node.clientTop
  };
}

export { withBrush, BoxBrush, constrainToRegion, getCoordsFromEvent };
