import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IForm } from 'app/entities/form/form.model';

export interface IYorum {
  id?: number;
  yazi?: string | null;
  date?: dayjs.Dayjs | null;
  userYorum?: IUser | null;
  formYorum?: IForm | null;
}

export class Yorum implements IYorum {
  constructor(
    public id?: number,
    public yazi?: string | null,
    public date?: dayjs.Dayjs | null,
    public userYorum?: IUser | null,
    public formYorum?: IForm | null
  ) {}
}

export function getYorumIdentifier(yorum: IYorum): number | undefined {
  return yorum.id;
}
