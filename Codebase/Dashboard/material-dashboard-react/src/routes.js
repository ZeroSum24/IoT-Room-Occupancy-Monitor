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
import UserProfile from "./views/UserProfile/UserProfile.jsx";
import TableList from "./views/TableList/TableList.jsx";
import Typography from "./views/Typography/Typography.jsx";
import Icons from "./views/Icons/Icons.jsx";
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
    path: "/table",
    name: "Occupancy Statistics",
    rtlName: "قائمة الجدول",
    icon: Person,
    component: Icons,
    layout: "/admin"
  },
  {
    path: "/user",
    name: "Space Usage",
    rtlName: "ملف تعريفي للمستخدم",
    icon: BubbleChart,
    component: UserProfile,
    layout: "/admin"
  },
  {
    path: "/typography",
    name: "Room Usage",
    rtlName: "طباعة",
    icon: LibraryBooks,
    component: Typography,
    layout: "/admin"
  },
  {
    path: "/icons",
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
    path: "/notifications",
    name: "Notifications",
    rtlName: "إخطارات",
    icon: Notifications,
    component: NotificationsPage,
    layout: "/admin"
  }
];

export default dashboardRoutes;
