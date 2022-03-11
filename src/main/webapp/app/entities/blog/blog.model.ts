import { IUser } from 'app/entities/user/user.model';

export interface IBlog {
  id?: number;
  name?: string | null;
  handle?: string | null;
  user?: IUser | null;
}

export class Blog implements IBlog {
  constructor(public id?: number, public name?: string | null, public handle?: string | null, public user?: IUser | null) {}
}

export function getBlogIdentifier(blog: IBlog): number | undefined {
  return blog.id;
}
