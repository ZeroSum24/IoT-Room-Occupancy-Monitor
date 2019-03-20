// @material-ui/icons
import Dashboard from "@material-ui/icons/Dashboard";
import Person from "@material-ui/icons/Person";
import LibraryBooks from "@material-ui/icons/LibraryBooks";
import BubbleChart from "@material-ui/icons/BubbleChart";
import LocationOn from "@material-ui/icons/LocationOn";
import Notifications from "@material-ui/icons/Notifications";
import Unarchive from "@material-ui/icons/Unarchive";
import Language from "@material-ui/icons/Language";
// core components/views for Admin layout
import DashboardPage from "./views/Dashboard/Dashboard.jsx";
import SpaceUsage from "./views/SpaceUsage/SpaceUsage.jsx";
import TableList from "./views/TableList/TableList.jsx";
import RoomUsage from "./views/RoomUsage/RoomUsage.jsx";
import OccupancyStatistics from "./views/OccupancyStatistics/OccupancyStatistics.jsx";
import IssuesList from "./views/IssuesList/IssuesList.jsx";
import Maps from "./views/Maps/Maps.jsx";
import NotificationsPage from "./views/Notifications/Notifications.jsx";
// core components/views for RTL layout
import RTLPage from "./views/RTLPage/RTLPage.jsx";

const dashboardRoutes = [
  {
    path: "/dashboard",
    name: "Dashboard",
    rtlName: "لوحة القيادة",
    icon: Dashboard,
    component: DashboardPage,
    layout: "/admin"
  },
  {
    path: "/occupancy_statistics",
    name: "Occupancy Statistics",
    rtlName: "قائمة الجدول",
    icon: Person,
    component: OccupancyStatistics,
    layout: "/admin"
  },
  {
    path: "/space_usage",
    name: "Space Usage",
    rtlName: "ملف تعريفي للمستخدم",
    icon: BubbleChart,
    component: SpaceUsage,
    layout: "/admin"
  },
  {
    path: "/room_usage",
    name: "Room Usage",
    rtlName: "طباعة",
    icon: LibraryBooks,
    component: RoomUsage,
    layout: "/admin"
  },
  {
    path: "/tables_list",
    name: "Tables List",
    rtlName: "الرموز",
    icon: "content_paste",
    component: TableList,
    layout: "/admin"
  },
  {
    path: "/maps",
    name: "Maps",
    rtlName: "خرائط",
    icon: LocationOn,
    component: Maps,
    layout: "/admin"
  },
  {
    path: "/issues_list",
    name: "Room Maintenance",
    rtlName: "الرموز",
    icon: Notifications,
    component: IssuesList,
    layout: "/admin"
  },
];

export default dashboardRoutes;
