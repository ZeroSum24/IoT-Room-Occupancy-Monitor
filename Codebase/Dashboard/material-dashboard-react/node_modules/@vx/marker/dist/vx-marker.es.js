import React from 'react';
import cx from 'classnames';
import PropTypes from 'prop-types';
import { Line } from '@vx/shape';
import { Group } from '@vx/group';

Marker.propTypes = {
  top: PropTypes.number,
  left: PropTypes.number,
  from: PropTypes.object,
  to: PropTypes.object,
  stroke: PropTypes.string,
  strokeWidth: PropTypes.number,
  strokeDasharray: PropTypes.string,
  transform: PropTypes.string,
  label: PropTypes.oneOfType([PropTypes.string, PropTypes.func]),
  labelAnchor: PropTypes.string,
  labelDx: PropTypes.number,
  labelDy: PropTypes.number,
  labelFill: PropTypes.string,
  labelStroke: PropTypes.string,
  labelStrokeWidth: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  labelFontSize: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  labelPaintOrder: PropTypes.string,
  className: PropTypes.string
};
function Marker(_ref) {
  var _ref$top = _ref.top,
      top = _ref$top === void 0 ? 0 : _ref$top,
      _ref$left = _ref.left,
      left = _ref$left === void 0 ? 0 : _ref$left,
      from = _ref.from,
      to = _ref.to,
      _ref$stroke = _ref.stroke,
      stroke = _ref$stroke === void 0 ? 'magenta' : _ref$stroke,
      _ref$strokeWidth = _ref.strokeWidth,
      strokeWidth = _ref$strokeWidth === void 0 ? 2 : _ref$strokeWidth,
      strokeDasharray = _ref.strokeDasharray,
      transform = _ref.transform,
      label = _ref.label,
      _ref$labelAnchor = _ref.labelAnchor,
      labelAnchor = _ref$labelAnchor === void 0 ? 'left' : _ref$labelAnchor,
      _ref$labelDx = _ref.labelDx,
      labelDx = _ref$labelDx === void 0 ? 0 : _ref$labelDx,
      _ref$labelDy = _ref.labelDy,
      labelDy = _ref$labelDy === void 0 ? 0 : _ref$labelDy,
      labelFill = _ref.labelFill,
      _ref$labelFontSize = _ref.labelFontSize,
      labelFontSize = _ref$labelFontSize === void 0 ? 10 : _ref$labelFontSize,
      _ref$labelStroke = _ref.labelStroke,
      labelStroke = _ref$labelStroke === void 0 ? 'white' : _ref$labelStroke,
      _ref$labelStrokeWidth = _ref.labelStrokeWidth,
      labelStrokeWidth = _ref$labelStrokeWidth === void 0 ? 3 : _ref$labelStrokeWidth,
      _ref$labelPaintOrder = _ref.labelPaintOrder,
      labelPaintOrder = _ref$labelPaintOrder === void 0 ? 'stroke' : _ref$labelPaintOrder,
      className = _ref.className;
  return React.createElement(Group, {
    top: top,
    left: left
  }, React.createElement(Line, {
    className: cx('vx-marker-line', className),
    from: from,
    to: to,
    stroke: stroke,
    strokeWidth: strokeWidth,
    strokeDasharray: strokeDasharray,
    transform: transform
  }), label && React.createElement("text", {
    x: from.x,
    y: from.y,
    dx: labelDx,
    dy: labelDy,
    fontSize: labelFontSize,
    fill: labelFill || stroke,
    stroke: labelStroke,
    strokeWidth: labelStrokeWidth,
    textAnchor: labelAnchor,
    paintOrder: labelPaintOrder
  }, label));
}

export { Marker };
