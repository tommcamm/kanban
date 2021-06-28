import { IUser } from '@/shared/model/user.model';

export interface IKanban {
  id?: number;
  name?: string;
  created_at?: Date | null;
  last_edit?: Date | null;
  userkanban?: IUser;
}

export class Kanban implements IKanban {
  constructor(
    public id?: number,
    public name?: string,
    public created_at?: Date | null,
    public last_edit?: Date | null,
    public userkanban?: IUser
  ) {}
}
