declare module 'react-router-dom' {
  import { ComponentType, ReactNode } from 'react';

  export interface NavigateFunction {
    (to: string, options?: { replace?: boolean }): void;
  }

  export interface Location {
    pathname: string;
    search: string;
    hash: string;
  }

  export function useNavigate(): NavigateFunction;
  export function useLocation(): Location;

  export interface RouteProps {
    path?: string;
    element?: ReactNode;
    children?: ReactNode;
  }

  export interface RoutesProps {
    children: ReactNode;
  }

  export interface NavigateProps {
    to: string;
    replace?: boolean;
  }

  export interface OutletProps {
    context?: any;
  }

  export interface BrowserRouterProps {
    children: ReactNode;
  }

  export const Routes: ComponentType<RoutesProps>;
  export const Route: ComponentType<RouteProps>;
  export const Navigate: ComponentType<NavigateProps>;
  export const Outlet: ComponentType<OutletProps>;
  export const BrowserRouter: ComponentType<BrowserRouterProps>;
} 