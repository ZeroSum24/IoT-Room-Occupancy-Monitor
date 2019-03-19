import { Line } from '@vx/shape';
import { Point } from '@vx/point';
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

Rows.propTypes = {
  top: PropTypes.number,
  left: PropTypes.number,
  className: PropTypes.string,
  stroke: PropTypes.string,
  strokeWidth: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  strokeDasharray: PropTypes.string,
  numTicks: PropTypes.number,
  lineStyle: PropTypes.object,
  offset: PropTypes.number,
  scale: PropTypes.func.isRequired,
  width: PropTypes.number.isRequired,
  tickValues: PropTypes.array
};
function Rows(_ref) {
  var _ref$top = _ref.top,
      top = _ref$top === void 0 ? 0 : _ref$top,
      _ref$left = _ref.left,
      left = _ref$left === void 0 ? 0 : _ref$left,
      scale = _ref.scale,
      width = _ref.width,
      _ref$stroke = _ref.stroke,
      stroke = _ref$stroke === void 0 ? '#eaf0f6' : _ref$stroke,
      _ref$strokeWidth = _ref.strokeWidth,
      strokeWidth = _ref$strokeWidth === void 0 ? 1 : _ref$strokeWidth,
      strokeDasharray = _ref.strokeDasharray,
      className = _ref.className,
      _ref$numTicks = _ref.numTicks,
      numTicks = _ref$numTicks === void 0 ? 10 : _ref$numTicks,
      lineStyle = _ref.lineStyle,
      offset = _ref.offset,
      tickValues = _ref.tickValues,
      restProps = _objectWithoutProperties(_ref, ["top", "left", "scale", "width", "stroke", "strokeWidth", "strokeDasharray", "className", "numTicks", "lineStyle", "offset", "tickValues"]);

  var ticks = scale.ticks ? scale.ticks(numTicks) : scale.domain();
  if (tickValues) ticks = tickValues;
  return React.createElement(Group, {
    className: cx('vx-rows', className),
    top: top,
    left: left
  }, ticks.map(function (d, i) {
    var y = offset ? scale(d) + offset : scale(d);
    var fromPoint = new Point({
      x: 0,
      y: y
    });
    var toPoint = new Point({
      x: width,
      y: y
    });
    return React.createElement(Line, _extends({
      key: "row-line-".concat(d, "-").concat(i),
      from: fromPoint,
      to: toPoint,
      stroke: stroke,
      strokeWidth: strokeWidth,
      strokeDasharray: strokeDasharray,
      style: lineStyle
    }, restProps));
  }));
}

Columns.propTypes = {
  top: PropTypes.number,
  left: PropTypes.number,
  className: PropTypes.string,
  stroke: PropTypes.string,
  strokeWidth: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  strokeDasharray: PropTypes.string,
  numTicks: PropTypes.number,
  lineStyle: PropTypes.object,
  offset: PropTypes.number,
  scale: PropTypes.func.isRequired,
  height: PropTypes.number.isRequired,
  tickValues: PropTypes.array
};
function Columns(_ref) {
  var _ref$top = _ref.top,
      top = _ref$top === void 0 ? 0 : _ref$top,
      _ref$left = _ref.left,
      left = _ref$left === void 0 ? 0 : _ref$left,
      scale = _ref.scale,
      height = _ref.height,
      _ref$stroke = _ref.stroke,
      stroke = _ref$stroke === void 0 ? '#eaf0f6' : _ref$stroke,
      _ref$strokeWidth = _ref.strokeWidth,
      strokeWidth = _ref$strokeWidth === void 0 ? 1 : _ref$strokeWidth,
      strokeDasharray = _ref.strokeDasharray,
      className = _ref.className,
      _ref$numTicks = _ref.numTicks,
      numTicks = _ref$numTicks === void 0 ? 10 : _ref$numTicks,
      lineStyle = _ref.lineStyle,
      offset = _ref.offset,
      tickValues = _ref.tickValues,
      restProps = _objectWithoutProperties(_ref, ["top", "left", "scale", "height", "stroke", "strokeWidth", "strokeDasharray", "className", "numTicks", "lineStyle", "offset", "tickValues"]);

  var ticks = scale.ticks ? scale.ticks(numTicks) : scale.domain();
  if (tickValues) ticks = tickValues;
  return React.createElement(Group, {
    className: cx('vx-columns', className),
    top: top,
    left: left
  }, ticks.map(function (d, i) {
    var x = offset ? scale(d) + offset : scale(d);
    var fromPoint = new Point({
      x: x,
      y: 0
    });
    var toPoint = new Point({
      x: x,
      y: height
    });
    return React.createElement(Line, _extends({
      key: "column-line-".concat(d, "-").concat(i),
      from: fromPoint,
      to: toPoint,
      stroke: stroke,
      strokeWidth: strokeWidth,
      strokeDasharray: strokeDasharray,
      style: lineStyle
    }, restProps));
  }));
}

Grid.propTypes = {
  top: PropTypes.number,
  left: PropTypes.number,
  className: PropTypes.string,
  stroke: PropTypes.string,
  strokeWidth: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  strokeDasharray: PropTypes.string,
  numTicksRows: PropTypes.number,
  numTicksColumns: PropTypes.number,
  rowLineStyle: PropTypes.object,
  columnLineStyle: PropTypes.object,
  xOffset: PropTypes.number,
  yOffset: PropTypes.number,
  xScale: PropTypes.func.isRequired,
  yScale: PropTypes.func.isRequired,
  height: PropTypes.number.isRequired,
  width: PropTypes.number.isRequired,
  rowTickValues: PropTypes.array,
  columnTickValues: PropTypes.array
};
function Grid(_ref) {
  var top = _ref.top,
      left = _ref.left,
      xScale = _ref.xScale,
      yScale = _ref.yScale,
      width = _ref.width,
      height = _ref.height,
      className = _ref.className,
      stroke = _ref.stroke,
      strokeWidth = _ref.strokeWidth,
      strokeDasharray = _ref.strokeDasharray,
      numTicksRows = _ref.numTicksRows,
      numTicksColumns = _ref.numTicksColumns,
      rowLineStyle = _ref.rowLineStyle,
      columnLineStyle = _ref.columnLineStyle,
      xOffset = _ref.xOffset,
      yOffset = _ref.yOffset,
      rowTickValues = _ref.rowTickValues,
      columnTickValues = _ref.columnTickValues,
      restProps = _objectWithoutProperties(_ref, ["top", "left", "xScale", "yScale", "width", "height", "className", "stroke", "strokeWidth", "strokeDasharray", "numTicksRows", "numTicksColumns", "rowLineStyle", "columnLineStyle", "xOffset", "yOffset", "rowTickValues", "columnTickValues"]);

  return React.createElement(Group, {
    className: cx('vx-grid', className),
    top: top,
    left: left
  }, React.createElement(Rows, _extends({
    className: className,
    scale: yScale,
    width: width,
    stroke: stroke,
    strokeWidth: strokeWidth,
    strokeDasharray: strokeDasharray,
    numTicks: numTicksRows,
    style: rowLineStyle,
    offset: yOffset,
    tickValues: rowTickValues
  }, restProps)), React.createElement(Columns, _extends({
    className: className,
    scale: xScale,
    height: height,
    stroke: stroke,
    strokeWidth: strokeWidth,
    strokeDasharray: strokeDasharray,
    numTicks: numTicksColumns,
    style: columnLineStyle,
    offset: xOffset,
    tickValues: columnTickValues
  }, restProps)));
}

export { Rows as GridRows, Columns as GridColumns, Grid };
