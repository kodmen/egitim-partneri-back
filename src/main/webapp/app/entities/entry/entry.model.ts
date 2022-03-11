import dayjs from 'dayjs/esm';
import { IBlog } from 'app/entities/blog/blog.model';
import { ITag } from 'app/entities/tag/tag.model';

export interface IEntry {
  id?: number;
  title?: string | null;
  content?: string | null;
  date?: dayjs.Dayjs | null;
  fotoContentType?: string | null;
  foto?: string | null;
  blog?: IBlog | null;
  tags?: ITag[] | null;
}

export class Entry implements IEntry {
  constructor(
    public id?: number,
    public title?: string | null,
    public content?: string | null,
    public date?: dayjs.Dayjs | null,
    public fotoContentType?: string | null,
    public foto?: string | null,
    public blog?: IBlog | null,
    public tags?: ITag[] | null
  ) {}
}

export function getEntryIdentifier(entry: IEntry): number | undefined {
  return entry.id;
}
