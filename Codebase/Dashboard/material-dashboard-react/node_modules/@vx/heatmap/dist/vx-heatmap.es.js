import React from 'react';
import cx from 'classnames';
import PropTypes from 'prop-types';
import { Group } from '@vx/group';

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

HeatmapCircle.propTypes = {
  data: PropTypes.array,
  left: PropTypes.number,
  top: PropTypes.number,
  gap: PropTypes.number,
  radius: PropTypes.number,
  xScale: PropTypes.func.isRequired,
  yScale: PropTypes.func.isRequired,
  colorScale: PropTypes.func,
  opacityScale: PropTypes.func,
  bins: PropTypes.func,
  count: PropTypes.func,
  className: PropTypes.string,
  children: PropTypes.func
};
function HeatmapCircle(_ref) {
  var className = _ref.className,
      top = _ref.top,
      left = _ref.left,
      data = _ref.data,
      _ref$gap = _ref.gap,
      gap = _ref$gap === void 0 ? 1 : _ref$gap,
      _ref$radius = _ref.radius,
      radius = _ref$radius === void 0 ? 6 : _ref$radius,
      xScale = _ref.xScale,
      yScale = _ref.yScale,
      _ref$colorScale = _ref.colorScale,
      colorScale = _ref$colorScale === void 0 ? function (d) {
    return undefined;
  } : _ref$colorScale,
      _ref$opacityScale = _ref.opacityScale,
      opacityScale = _ref$opacityScale === void 0 ? function (d) {
    return 1;
  } : _ref$opacityScale,
      _ref$bins = _ref.bins,
      bins = _ref$bins === void 0 ? function (d) {
    return d.bins;
  } : _ref$bins,
      _ref$count = _ref.count,
      count = _ref$count === void 0 ? function (d) {
    return d.count;
  } : _ref$count,
      children = _ref.children,
      restProps = _objectWithoutProperties(_ref, ["className", "top", "left", "data", "gap", "radius", "xScale", "yScale", "colorScale", "opacityScale", "bins", "count", "children"]);

  var r = radius - gap;
  var heatmap = data.map(function (datum, column) {
    var x = xScale(column);
    return bins(datum).map(function (bin, row) {
      var countValue = count(bin);
      return {
        bin: bin,
        row: row,
        column: column,
        datum: datum,
        r: r,
        radius: radius,
        gap: gap,
        count: countValue,
        cx: radius + x,
        cy: yScale(row) + gap + radius,
        opacity: opacityScale(countValue),
        color: colorScale(countValue)
      };
    });
  });
  if (children) return children(heatmap);
  return React.createElement(Group, {
    className: "vx-heatmap-circles",
    top: top,
    left: left
  }, heatmap.map(function (_bins) {
    return _bins.map(function (bin) {
      return React.createElement("circle", _extends({
        key: "heatmap-tile-circle-".concat(bin.row, "-").concat(bin.column),
        className: cx('vx-heatmap-circle', className),
        r: bin.r,
        cx: bin.cx,
        cy: bin.cy,
        fill: bin.color,
        fillOpacity: bin.opacity
      }, restProps));
    });
  }));
}

HeatmapRect.propTypes = {
  data: PropTypes.array,
  top: PropTypes.number,
  left: PropTypes.number,
  binWidth: PropTypes.number,
  binHeight: PropTypes.number,
  x0: PropTypes.number,
  gap: PropTypes.number,
  xScale: PropTypes.func,
  yScale: PropTypes.func,
  colorScale: PropTypes.func,
  opacityScale: PropTypes.func,
  bins: PropTypes.func,
  count: PropTypes.func,
  className: PropTypes.string,
  children: PropTypes.func
};
function HeatmapRect(_ref) {
  var className = _ref.className,
      top = _ref.top,
      left = _ref.left,
      data = _ref.data,
      binWidth = _ref.binWidth,
      binHeight = _ref.binHeight,
      _ref$x = _ref.x0,
      x0 = _ref$x === void 0 ? 0 : _ref$x,
      _ref$gap = _ref.gap,
      gap = _ref$gap === void 0 ? 1 : _ref$gap,
      xScale = _ref.xScale,
      yScale = _ref.yScale,
      _ref$colorScale = _ref.colorScale,
      colorScale = _ref$colorScale === void 0 ? function (d) {
    return undefined;
  } : _ref$colorScale,
      _ref$opacityScale = _ref.opacityScale,
      opacityScale = _ref$opacityScale === void 0 ? function (d) {
    return 1;
  } : _ref$opacityScale,
      _ref$bins = _ref.bins,
      bins = _ref$bins === void 0 ? function (d) {
    return d.bins;
  } : _ref$bins,
      _ref$count = _ref.count,
      count = _ref$count === void 0 ? function (d) {
    return d.count;
  } : _ref$count,
      children = _ref.children,
      restProps = _objectWithoutProperties(_ref, ["className", "top", "left", "data", "binWidth", "binHeight", "x0", "gap", "xScale", "yScale", "colorScale", "opacityScale", "bins", "count", "children"]);

  var width = binWidth - gap;
  var height = binHeight - gap;
  var heatmap = data.map(function (datum, column) {
    var x = xScale(column);
    return bins(datum).map(function (bin, row) {
      var countValue = count(bin);
      return {
        bin: bin,
        row: row,
        column: column,
        datum: datum,
        width: width,
        height: height,
        gap: gap,
        count: countValue,
        x: x + x0,
        y: yScale(row) + gap,
        color: colorScale(countValue),
        opacity: opacityScale(countValue)
      };
    });
  });
  if (children) return children(heatmap);
  return React.createElement(Group, {
    className: "vx-heatmap-rects",
    top: top,
    left: left
  }, heatmap.map(function (_bins, column) {
    return _bins.map(function (bin) {
      return React.createElement("rect", _extends({
        key: "heatmap-tile-rect-".concat(bin.row, "-").concat(bin.column),
        className: cx('vx-heatmap-rect', className),
        width: bin.width,
        height: bin.height,
        x: bin.x + bin.x0,
        y: bin.y,
        fill: bin.color,
        fillOpacity: bin.opacity
      }, restProps));
    });
  }));
}

export { HeatmapCircle, HeatmapRect };
