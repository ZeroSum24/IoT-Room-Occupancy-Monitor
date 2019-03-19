import cx from 'classnames';
import { Group } from '@vx/group';
import PropTypes from 'prop-types';
import React from 'react';

Links.propTypes = {
  links: PropTypes.array,
  linkComponent: PropTypes.any,
  className: PropTypes.string
};
function Links(_ref) {
  var links = _ref.links,
      linkComponent = _ref.linkComponent,
      className = _ref.className;
  return React.createElement(Group, null, links.map(function (link, i) {
    return React.createElement(Group, {
      className: cx('vx-network-links', className),
      key: "network-link-".concat(i)
    }, React.createElement(linkComponent, {
      link: link
    }));
  }));
}

Nodes.propTypes = {
  nodes: PropTypes.array,
  nodeComponent: PropTypes.any,
  className: PropTypes.string
};
function Nodes(_ref) {
  var nodes = _ref.nodes,
      nodeComponent = _ref.nodeComponent,
      className = _ref.className;
  return React.createElement(Group, null, nodes.map(function (node, i) {
    return React.createElement(Group, {
      key: "network-node-".concat(i),
      className: cx('vx-network-nodes', className),
      transform: "translate(".concat(node.x, ", ").concat(node.y, ")")
    }, React.createElement(nodeComponent, {
      node: node
    }));
  }));
}

DefaultLink.propTypes = {
  link: PropTypes.object
};
function DefaultLink(_ref) {
  var link = _ref.link;
  return React.createElement("line", {
    x1: link.source.x,
    y1: link.source.y,
    x2: link.target.x,
    y2: link.target.y,
    strokeWidth: 2,
    stroke: "#999",
    strokeOpacity: 0.6
  });
}

function DefaultNode() {
  return React.createElement("circle", {
    r: 15,
    fill: "#21D4FD"
  });
}

Graph.propTypes = {
  graph: PropTypes.object,
  linkComponent: PropTypes.any,
  nodeComponent: PropTypes.any
};
function Graph(_ref) {
  var graph = _ref.graph,
      _ref$linkComponent = _ref.linkComponent,
      linkComponent = _ref$linkComponent === void 0 ? DefaultLink : _ref$linkComponent,
      _ref$nodeComponent = _ref.nodeComponent,
      nodeComponent = _ref$nodeComponent === void 0 ? DefaultNode : _ref$nodeComponent;
  return React.createElement(Group, null, React.createElement(Links, {
    links: graph.links,
    linkComponent: linkComponent
  }), React.createElement(Nodes, {
    nodes: graph.nodes,
    nodeComponent: nodeComponent
  }));
}

export { Graph, Links, Nodes, DefaultLink, DefaultNode };
