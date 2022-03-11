import { IEntry } from 'app/entities/entry/entry.model';

export interface ITag {
  id?: number;
  name?: string | null;
  entries?: IEntry[] | null;
}

export class Tag implements ITag {
  constructor(public id?: number, public name?: string | null, public entries?: IEntry[] | null) {}
}

export function getTagIdentifier(tag: ITag): number | undefined {
  return tag.id;
}
