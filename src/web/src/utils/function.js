import config from '../config';
import { WELCOME_PAGE_FUNCTION_CODE } from '../constants';

export function resolveAction(func) {
  if (!func.action) {
    if (process.env.NODE_ENV === 'development') {
      if (config.devComm.proxy === true) {
        return `${config.devComm.urlPrefix}/ui/${func.serviceId}/index.html#/${func.code}`;
      }
    } else {
      return `/ui/${func.serviceId}/index.html#/${func.code}`;
    }
  } else {
    return func.action;
  }
}

/**
 * 平台默认的首页.
 */
export const defaultHomeFunction = {
  id: WELCOME_PAGE_FUNCTION_CODE,
  name: '首页',
  code: WELCOME_PAGE_FUNCTION_CODE,
  serviceId: 'reap-portal',
};
