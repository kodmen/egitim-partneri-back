import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RozetComponent } from './list/rozet.component';
import { RozetDetailComponent } from './detail/rozet-detail.component';
import { RozetUpdateComponent } from './update/rozet-update.component';
import { RozetDeleteDialogComponent } from './delete/rozet-delete-dialog.component';
import { RozetRoutingModule } from './route/rozet-routing.module';

@NgModule({
  imports: [SharedModule, RozetRoutingModule],
  declarations: [RozetComponent, RozetDetailComponent, RozetUpdateComponent, RozetDeleteDialogComponent],
  entryComponents: [RozetDeleteDialogComponent],
})
export class RozetModule {}
