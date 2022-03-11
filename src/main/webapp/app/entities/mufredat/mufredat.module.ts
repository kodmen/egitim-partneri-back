import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MufredatComponent } from './list/mufredat.component';
import { MufredatDetailComponent } from './detail/mufredat-detail.component';
import { MufredatUpdateComponent } from './update/mufredat-update.component';
import { MufredatDeleteDialogComponent } from './delete/mufredat-delete-dialog.component';
import { MufredatRoutingModule } from './route/mufredat-routing.module';

@NgModule({
  imports: [SharedModule, MufredatRoutingModule],
  declarations: [MufredatComponent, MufredatDetailComponent, MufredatUpdateComponent, MufredatDeleteDialogComponent],
  entryComponents: [MufredatDeleteDialogComponent],
})
export class MufredatModule {}
